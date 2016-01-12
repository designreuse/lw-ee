package ru.tsystems.javaschool.kuzmenkov.logiweb.services;

import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.DriverDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.DriverShift;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

/**
 * Data manipulation and business logic related to drivers.
 *
 * @author Nikolay Kuzmenkov.
 */
public interface DriverService {

    /**
     * Add new driver.
     *
     * @param newDriverDTO newDriverDTO
     * @return same Driver
     * @throws LogiwebValidationException if driver don't have all required fields or have not unique personal number.
     * @throws LogiwebDAOException if unexpected exception occurred on lower level (not user fault).
     */
    Integer addNewDriver(DriverDTO newDriverDTO) throws LogiwebDAOException, LogiwebValidationException, NoSuchAlgorithmException;

    /**
     * Calculate working hours for driver for this month.
     *
     * Shift records that are started in previous month are trimmed.
     * (Date of start is set to first day of the this month and 00:00 hours)
     *
     * Records that don't have ending date (meaning that driver is currently on shift)
     * are also counted. End time for them is current time.
     *
     * @param driverId
     * @throws LogiwebDAOException if unexpected exception on lower level occurred (not user fault).
     */
    Float calculateWorkingHoursForDriver(Integer driverId) throws LogiwebDAOException;

    /**
     * Assign driver to truck.
     *
     * @param driverId
     * @param truckId
     * @throws LogiwebValidationException if truck or diver not exist, or if truck already havefull driver count assigned.
     * @throws LogiwebDAOException if unexpected exception on lower level occurred (not user fault).
     */
    void assignDriverToTruck(Integer driverId, Integer truckId) throws LogiwebDAOException, LogiwebValidationException;

    /**
     * Edit driver.
     *
     * @param editedDriverDTO
     * @throws LogiwebDAOException
     */
    void editDriver(DriverDTO editedDriverDTO) throws LogiwebDAOException, LogiwebValidationException;

    /**
     * Find drivers.
     *
     * @return empty list if nothing found.
     * @throws LogiwebDAOException if unexpected exception occurred on lower level (not user fault).
     */
    Set<DriverDTO> findAllDrivers() throws LogiwebDAOException;

    /**
     * Find driver by id.
     *
     * @param driverId
     * @throws LogiwebDAOException if unexpected exception occurred on lower level (not user fault).
     */
    DriverDTO findDriverById(Integer driverId) throws LogiwebDAOException;

    /**
     * Find shift records that are started or ended in this month. Records are
     * not trimmed. (Meaning that if record is started in previous month then it
     * will be show 'as is').
     *
     * @param driverId
     * @return shift records or empty set
     * @throws LogiwebDAOException
     *             if unexpected exception on lower level occurred (not user
     *             fault)
     */
    List<DriverShift> findDriverShiftRecordsForThisMonth(Integer driverId) throws LogiwebDAOException;

    /**
     * Delete driver.
     *
     * @param driverId
     * @throws LogiwebValidationException if driver is attached to truck.
     * @throws LogiwebDAOException
     *             if unexpected exception on lower level occurred (not user
     *             fault).
     */
    void deleteDriver(Integer driverId) throws LogiwebDAOException, LogiwebValidationException;

    Set<DriverDTO> findUnassignedDriversByWorkingHoursAndCity(Integer cityId, Float maxWorkingHours)
            throws LogiwebDAOException;

    /**
     * Start new shift and change driver status to Resting en route.
     *
     * @param driverNumber
     * @throws LogiwebValidationException
     *             if unfinished shift for this driver is exist. Or if driver
     *             does not exist. Or if driver status is not FREE.
     * @throws LogiwebDAOException
     *             if unexpected exception on lower level occurred (not user
     *             fault)
     */
    void startShiftForDriver(Integer driverNumber) throws LogiwebDAOException, LogiwebValidationException;

    /**
     * End shift and change driver status to Free.
     *
     * @param driverNumber
     * @throws LogiwebValidationException
     *             if there is no unfinished shift for this driver. Or if driver
     *             does not exist.
     * @throws LogiwebDAOException
     *             if unexpected exception on lower level occurred (not user
     *             fault)
     */
    void endShiftForDriver(Integer driverNumber) throws LogiwebDAOException, LogiwebValidationException;

    void setStatusDrivingForDriver(Integer driverNumber) throws LogiwebDAOException;

    void setStatusRestingForDriver(Integer driverNumber)throws LogiwebDAOException;

    DriverDTO getDriverWithFullInfo(Integer driverId) throws LogiwebDAOException;

    /**
     * Find driver by personal number.
     *
     * @param personalNumber
     * @return driver or null
     * @throws LogiwebDAOException
     *             if unexpected exception occurred on lower level (not user
     *             fault)
     */
    Driver getDriverByPersonalNumber(Integer personalNumber) throws LogiwebDAOException;
}
