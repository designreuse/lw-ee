package ru.tsystems.javaschool.kuzmenkov.logiweb.ws;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.FreightService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.UserService;

import javax.jws.WebService;
import javax.ws.rs.ServerErrorException;

/**
 * @author Nikolay Kuzmenkov.
 */
@WebService(endpointInterface = "ru.tsystems.javaschool.kuzmenkov.logiweb.ws.WsActions")
public class WsActionsImpl implements WsActions {

    private static final Logger LOGGER = Logger.getLogger(WsActionsImpl.class);

    @Autowired
    private DriverService driverService;
    @Autowired
    private FreightService freightService;
    @Autowired
    private UserService userService;

    @Override
    public void startShiftForDriver(Integer driverNumber) throws LogiwebValidationException {
        try {
            driverService.startShiftForDriver(driverNumber);

        } catch (LogiwebServiceException e) {
            LOGGER.warn("Something unexpected happen", e);
            throw new ServerErrorException(500);
        }
    }

    @Override
    public void endShiftForDriver(Integer driverNumber) throws LogiwebValidationException {
        try {
            driverService.endShiftForDriver(driverNumber);

        } catch (LogiwebServiceException e) {
            LOGGER.warn("Something unexpected happen", e);
            throw new ServerErrorException(500);
        }
    }

    @Override
    public void setStatusDrivingForDriver(Integer driverNumber) {
        try {
            driverService.setStatusDrivingForDriver(driverNumber);
        } catch (LogiwebServiceException e) {
            LOGGER.warn("Something unexpected happen", e);
            throw new ServerErrorException(500);
        }
    }

    @Override
    public void setStatusPickedUpForFreight(Integer freightId) throws IllegalStateException {
        try {
            freightService.setPickedUpStatus(freightId);

        } catch (LogiwebServiceException e) {
            LOGGER.warn("Something unexpected happen", e);
            throw new ServerErrorException(500);
        }
    }

    @Override
    public DriverInfo authenticateDriver(Integer driverId, String driverPassword) throws LogiwebServiceException {
        System.out.println("1");
        Driver driver = driverService.getDriverByPersonalNumber(driverId);
        System.out.println("2");

        DriverInfo driverInfo = new DriverInfo();

        //&& userService.getMD5Hash(driverPassword).equals(driver.getLogiwebDriverAccount().getUserPassword())
        if (driver != null) {
            System.out.println("3");
            driverInfo.setFirstName(driver.getFirstName());
            driverInfo.setLastName(driver.getLastName());
            return driverInfo;
        }

        return null;
    }

    @Override
    public int say(String name) {
        System.out.println("Hello " + name + "!");

        return 5;
    }
}
