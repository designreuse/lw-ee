package ru.tsystems.javaschool.kuzmenkov.logiweb.ws;

import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author Nikolay Kuzmenkov.
 */
@WebService
public interface WsActions {

    /**
     * Start new shift for driver.
     *
     * @param driverNumber
     * @throws LogiwebValidationException if unfinished shift for this driver is exist. Or if driver
     *             does not exist. Or if driver status is not FREE.
     */
    void startShiftForDriver(@WebParam(name = "DriverPersonalNumber") Integer driverNumber) throws LogiwebValidationException;

    /**
     * End shift for driver.
     *
     * @param driverNumber
     * @throws LogiwebValidationException if there is no unfinished shift for this driver. Or if driver
     *             does not exist.
     */
    void endShiftForDriver(@WebParam(name = "DriverPersonalNumber") Integer driverNumber) throws LogiwebValidationException;

    void setStatusDrivingForDriver(@WebParam(name = "DriverPersonalNumber") Integer driverNumber);

    /**
     * Set 'Picked up' status for freight.
     *
     * @param freightId
     * @throws IllegalStateException
     *             if cargo is not in 'Ready for pickup' state or order is not in
     *             'Ready to go' state
     */
    void setStatusPickedUpForFreight(@WebParam(name = "FreightId") Integer freightId) throws IllegalStateException;
}
