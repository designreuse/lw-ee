package ru.tsystems.javaschool.kuzmenkov.logiweb.ws;


import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

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
    void endShiftForDriver(@WebParam(name = "DriverPersonalNumber") Integer driverPersonalNumber)
            ;

    void setStatusRestingForDriver(@WebParam(name = "DriverPersonalNumber") Integer driverPersonalNumber);

    void setStatusDrivingForDriver(@WebParam(name = "DriverPersonalNumber") Integer driverPersonalNumber);


    /*
    @WebMethod
    String setDriverStatus(@WebParam(name = "driverId") int driverId,
                           @WebParam(name = "driverStatus") String driverStatus);


    @WebMethod
    String getDriverStatus(@WebParam(name = "driverId") int driverId) throws LogiwebServiceException;
    */

    /**
     * Takes a driver credentials and processes authentication using SOAP webservice.
     */
    @WebMethod
    Driver authenticateDriver(@WebParam(name = "driverPersonalNumber") Integer driverPersonalNumber,
                              @WebParam(name = "driverPassword") String driverPassword) throws LogiwebServiceException;
}
