package ru.tsystems.javaschool.kuzmenkov.logiweb.ws;

import org.springframework.beans.factory.annotation.Autowired;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.OrderRoute;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.FreightService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.OrderService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.TruckService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.PasswordConverter;

import javax.jws.WebService;
import java.security.NoSuchAlgorithmException;

/**
 * SOAP webservice implementation to manage driver status changes from the client app.
 */
@WebService(endpointInterface="ru.tsystems.javaschool.kuzmenkov.logiweb.ws.DriverWebService")
public class DriverWebServiceImpl implements DriverWebService {

    @Autowired
    private DriverService driverService;
    @Autowired
    private FreightService freightService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TruckService truckService;

    /**
     * Start new shift for driver.
     *
     * @param driverPersonalNumber
     * @throws LogiwebValidationException if unfinished shift for this driver is exist. Or if driver
     *             does not exist. Or if driver status is not FREE.
     */
    @Override
    public void startShiftForDriver(Integer driverPersonalNumber) throws LogiwebValidationException {
        driverService.startShiftForDriver(driverPersonalNumber);
    }

    /**
     * End shift for driver.
     *
     * @param driverPersonalNumber
     *
     */
    @Override
    public void endShiftForDriver(Integer driverPersonalNumber) throws LogiwebValidationException {
        driverService.endShiftForDriver(driverPersonalNumber);
    }

    @Override
    public void setStatusRestingForDriver(Integer driverPersonalNumber) {
        driverService.setStatusRestingForDriver(driverPersonalNumber);
    }

    @Override
    public void setStatusDrivingForDriver(Integer driverPersonalNumber) {
        driverService.setStatusDrivingForDriver(driverPersonalNumber);
    }

    /**
     * Takes a driver credentials and processes authentication using SOAP webservice.
     */
    @Override
    public Driver authenticateDriver(Integer driverPersonalNumber, String driverPassword) throws NoSuchAlgorithmException {
        Driver driver = driverService.getDriverByPersonalNumber(driverPersonalNumber);

        if (driver != null && driver.getLogiwebDriverAccount().getUserPassword().
                equals(PasswordConverter.getMD5Hash(driverPassword))) {
            return driver;
        }

        return null;
    }


    @Override
    public DriverInfo getDriverInfo(Integer driverPersonalNumber) {
        Driver driver = driverService.getDriverByPersonalNumber(driverPersonalNumber);

        DriverInfo infoForDriver = new DriverInfo();

        infoForDriver.setPersonalNumber(driver.getPersonalNumber());
        infoForDriver.setCurrentDriverStatus(driver.getDriverStatus());
        infoForDriver.setFirstName(driver.getFirstName());
        infoForDriver.setLastName(driver.getLastName());
        infoForDriver.setWorkingHoursInThisMonth(driverService
                .calculateWorkingHoursForDriver(driver.getDriverId()));

        if (driver.getCurrentTruckFK() != null
                && driver.getCurrentTruckFK().getOrderForThisTruck() != null) {
            OrderRoute routeInfo = freightService.getRouteInformationForOrder(driver.getCurrentTruckFK()
                    .getOrderForThisTruck().getOrderId());
            infoForDriver.setOrderWayPoints(routeInfo.getBestOrderOfDelivery());

            infoForDriver.setOrderStatus(driver.getCurrentTruckFK().getOrderForThisTruck().getOrderStatus());
        }

        return infoForDriver;
    }

    /**
     * Set 'Picked up' status for freight.
     *
     * @param freightId
     *
     */
    @Override
    public void setStatusPickUpForFreight(Integer freightId) {
        freightService.setPickUpStatus(freightId);
    }

    @Override
    public void setStatusDeliverForFreightAndEndCurrentOrderIfPossible(Integer freightId,
                                                                       Integer driverPersonalNumber) throws LogiwebValidationException {

            freightService.setDeliverStatus(freightId, driverPersonalNumber);

            Order order = freightService.findFreightById(freightId).getOrderForThisFreightFK();
            Truck assignedToTruck = order.getAssignedTruckFK();
            Integer orderId = order.getOrderId();
            if (orderService.isAllFreightsInOrderDelivered(orderId)) {
                orderService.setStatusDeliveredForOrder(orderId);
                truckService.removeAssignedOrderAndDriversFromTruck(assignedToTruck.getTruckId());
            }
    }
}
