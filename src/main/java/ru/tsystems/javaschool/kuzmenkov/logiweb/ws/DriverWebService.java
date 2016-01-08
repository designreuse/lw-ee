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
    void startShiftForDriver(@WebParam(name = "DriverPersonalNumber") Integer driverPersonalNumber) throws LogiwebValidationException;
    /**
     * Takes an id and a status of a driver and responses with the driver status using SOAP webservice.
     */
    @WebMethod
    String setDriverStatus(@WebParam(name = "driverId") int driverId,
                           @WebParam(name = "driverStatus") String driverStatus);

    /**
     * Takes a driver id and gets his/her status using SOAP webservice.
     */
    @WebMethod
    String getDriverStatus(@WebParam(name = "driverId") int driverId) throws LogiwebServiceException;

    /**
     * Takes a driver credentials and processes authentication using SOAP webservice.
     */
    @WebMethod
    Driver authenticateDriver(@WebParam(name = "driverPersonalNumber") Integer driverPersonalNumber,
                              @WebParam(name = "driverPassword") String driverPassword) throws LogiwebServiceException;
}
