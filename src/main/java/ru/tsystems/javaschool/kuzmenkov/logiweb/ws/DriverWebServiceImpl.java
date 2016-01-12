package ru.tsystems.javaschool.kuzmenkov.logiweb.ws;

import org.springframework.beans.factory.annotation.Autowired;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.OrderRoute;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.FreightService;
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

    @Override
    public void startShiftForDriver(Integer driverPersonalNumber) throws LogiwebValidationException {
        driverService.startShiftForDriver(driverPersonalNumber);
    }

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

    @Override
    public void setStatusPickUpForFreight(Integer freightId) {
        freightService.setPickUpStatus(freightId);
    }

    @Override
    public void setStatusDeliverForFreightAndEndCurrentOrderIfPossible(Integer freightId) {

            freightService.setDeliverStatus(freightId);

            /*Order order = freightService..findOrderById(cargoId).getOrderForThisCargo();
            Truck assignedToTruck = order.getAssignedTruck();
            int orderId = order.getId();
            if (ordersAndCargoService.isAllCargoesInOrderDelivered(orderId)) {
                ordersAndCargoService.setStatusDeliveredForOrder(orderId);
                truckService
                        .removeAssignedOrderAndDriversFromTruck(assignedToTruck
                                .getId());*/
    }
}
