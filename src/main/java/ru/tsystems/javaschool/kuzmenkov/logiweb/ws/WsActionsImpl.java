package ru.tsystems.javaschool.kuzmenkov.logiweb.ws;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.FreightService;

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
}
