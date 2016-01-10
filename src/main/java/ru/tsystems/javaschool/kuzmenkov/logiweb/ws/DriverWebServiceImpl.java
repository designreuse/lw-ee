package ru.tsystems.javaschool.kuzmenkov.logiweb.ws;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.FreightService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.OrderService;

import javax.jws.WebService;
import javax.ws.rs.ServerErrorException;

/**
 * SOAP webservice implementation to manage driver status changes from the client app.
 */
@WebService(endpointInterface="ru.tsystems.javaschool.kuzmenkov.logiweb.ws.DriverWebService")
public class DriverWebServiceImpl implements DriverWebService {

    private static final Logger LOGGER = Logger.getLogger(DriverWebServiceImpl.class);

    @Autowired
    private DriverService driverService;
    @Autowired
    private FreightService freightService;
    @Autowired
    private OrderService orderService;

    @Override
    public void startShiftForDriver(Integer driverPersonalNumber) throws LogiwebValidationException {
        try {
            driverService.startShiftForDriver(driverPersonalNumber);

        } catch (LogiwebServiceException e) {
            LOGGER.warn("Something unexpected happen", e);
            throw new ServerErrorException(500);
        }
    }

    @Override
    public void endShiftForDriver(Integer driverPersonalNumber) {
        try {
            driverService.endShiftForDriver(driverPersonalNumber);

        } catch (LogiwebServiceException e) {
            LOGGER.warn("Something unexpected happen", e);
            throw new ServerErrorException(500);
        }
    }

    @Override
    public void setStatusRestingForDriver(Integer driverPersonalNumber) {
        try {
            driverService.setStatusRestingForDriver(driverPersonalNumber);
        } catch (LogiwebServiceException e) {
            LOGGER.warn("Something unexpected happen", e);
            throw new ServerErrorException(500);
        }
    }

    @Override
    public void setStatusDrivingForDriver(Integer driverPersonalNumber) {
        try {
            driverService.setStatusDrivingForDriver(driverPersonalNumber);

        }  catch (LogiwebServiceException e) {
            LOGGER.warn("Something unexpected happen", e);
            throw new ServerErrorException(500);
        }
    }

    /**
     * Takes a driver credentials and processes authentication using SOAP webservice.
     */
    @Override
    public Driver authenticateDriver(Integer driverPersonalNumber, String driverPassword) throws LogiwebServiceException {
        Driver driver = driverService.getDriverByPersonalNumber(driverPersonalNumber);


        if (driver != null && driverPassword.equals("12345")) {
            return driver;
        }

        return null;
    }


    @Override
    public Driver getDriverInfo(Integer driverPersonalNumber) throws LogiwebServiceException {
        Driver driver = driverService.getDriverByPersonalNumber(driverPersonalNumber);

        if (driver != null) {
            return driver;
        }

        return null;
    }

    @Override
    public void setStatusPickUpForFreight(Integer freightId) {
        try {
            freightService.setPickUpStatus(freightId);
        } catch (LogiwebServiceException e) {
            LOGGER.warn("Something unexpected happen", e);
            throw new ServerErrorException(500);
        }
    }

    @Override
    public void setStatusDeliverForFreightAndEndCurrentOrderIfPossible(Integer freightId) throws LogiwebServiceException {
        try {
            freightService.setDeliverStatus(freightId);

            /*Order order = freightService..findOrderById(cargoId).getOrderForThisCargo();
            Truck assignedToTruck = order.getAssignedTruck();
            int orderId = order.getId();
            if (ordersAndCargoService.isAllCargoesInOrderDelivered(orderId)) {
                ordersAndCargoService.setStatusDeliveredForOrder(orderId);
                truckService
                        .removeAssignedOrderAndDriversFromTruck(assignedToTruck
                                .getId());*/
            } catch (LogiwebServiceException e) {
            LOGGER.warn("Something unexpected happen", e);
            throw new ServerErrorException(500);
        }
    }
}
