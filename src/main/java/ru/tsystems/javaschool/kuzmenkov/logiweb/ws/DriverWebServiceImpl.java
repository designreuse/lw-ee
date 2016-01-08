package ru.tsystems.javaschool.kuzmenkov.logiweb.ws;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;

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

    }

    @Override
    public void setStatusRestingForDriver(Integer driverPersonalNumber) {

    }

    @Override
    public void setStatusDrivingForDriver(Integer driverPersonalNumber) {

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
}
