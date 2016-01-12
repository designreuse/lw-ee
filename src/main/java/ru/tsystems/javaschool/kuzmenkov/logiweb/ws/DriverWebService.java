package ru.tsystems.javaschool.kuzmenkov.logiweb.ws;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.security.NoSuchAlgorithmException;

/**
 * Service Endpoint Interface (SEI).
 */
@WebService
public interface DriverWebService {

    /**
     * Start new shift for driver.
     *
     * @param driverPersonalNumber
     * @throws LogiwebValidationException if unfinished shift for this driver is exist. Or if driver
     *             does not exist. Or if driver status is not FREE.
     */
    void startShiftForDriver(@WebParam(name = "DriverPersonalNumber") Integer driverPersonalNumber)
            throws LogiwebValidationException;

    /**
     * End shift for driver.
     *
     * @param driverPersonalNumber
     *
     */
    void endShiftForDriver(@WebParam(name = "DriverPersonalNumber") Integer driverPersonalNumber) throws LogiwebValidationException
    ;

    void setStatusRestingForDriver(@WebParam(name = "DriverPersonalNumber") Integer driverPersonalNumber);

    void setStatusDrivingForDriver(@WebParam(name = "DriverPersonalNumber") Integer driverPersonalNumber);

    /**
     * Takes a driver credentials and processes authentication using SOAP webservice.
     *
     */
    Driver authenticateDriver(@WebParam(name = "driverPersonalNumber") Integer driverPersonalNumber,
                              @WebParam(name = "driverPassword") String driverPassword) throws NoSuchAlgorithmException;


    DriverInfo getDriverInfo(@WebParam(name = "driverPersonalNumber") Integer driverPersonalNumber);

    /**
     * Set 'Picked up' status for freight.
     *
     * @param freightId
     *
     */
    void setStatusPickUpForFreight(@WebParam(name = "freightId") Integer freightId);

    void setStatusDeliverForFreightAndEndCurrentOrderIfPossible(
            @WebParam(name = "freightId") Integer freightId,
            @WebParam(name = "driverPersonalNumber") Integer driverPersonalNumber) throws LogiwebValidationException;
}
