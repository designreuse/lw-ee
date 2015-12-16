package ru.tsystems.javaschool.kuzmenkov.logiweb.services.Impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.kuzmenkov.logiweb.controllers.model.ModelAttributeDriver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.DriverDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.DriverShiftDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.TruckDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.*;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.DriverStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.Role;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.UserService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.DateUtil;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebValidator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Data manipulation and business logic related to drivers.
 *
 * @author Nikolay Kuzmenkov.
 */
@Service("driverService")
public class DriverServiceImpl implements DriverService {

    private static final Logger LOGGER = Logger.getLogger(DriverServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    UserService userService;
    @Autowired
    private DriverDAO driverDAO;
    @Autowired
    private TruckDAO truckDAO;
    @Autowired
    private DriverShiftDAO driverShiftDAO;

    @Override
    @Transactional
    public Integer addNewDriver(ModelAttributeDriver driverFromForm) throws LogiwebServiceException, LogiwebValidationException {
        try {
            String personalNumberAsString = String.valueOf(driverFromForm.getPersonalNumber());
            String driverAccountEmail = "driver" + personalNumberAsString + "@logiweb.com";
            String driverAccountPassword = "12345";

            Integer newDriverUserId = userService.createNewUser(driverAccountEmail, driverAccountPassword, Role.ROLE_DRIVER);
            User accountForDriver = userService.findUserById(newDriverUserId);

            Driver driverWithSamePersonalNumber = driverDAO.findDriverByPersonalNumber(driverFromForm.getPersonalNumber());

            if (driverWithSamePersonalNumber != null) {
                throw new LogiwebValidationException("Driver with this personal number #" + driverFromForm.getPersonalNumber()
                        + " is already exist.");
            }

            Driver newDriverEntity = new Driver();
            createEntityDriverFromModelAttribute(newDriverEntity, driverFromForm);
            newDriverEntity.setLogiwebDriverAccount(accountForDriver);
            newDriverEntity.setDriverStatus(DriverStatus.FREE);

            driverDAO.create(newDriverEntity);

            LOGGER.info("Driver created: " + newDriverEntity.getFirstName() + " " + newDriverEntity.getLastName()
                    + " personal number #" + newDriverEntity.getPersonalNumber() + " , ID: " + newDriverEntity.getDriverId());

            return newDriverEntity.getDriverId();

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in DriverServiceImpl - addNewDriver().", e);
            throw new LogiwebServiceException(e);
        }
    }

    private Driver createEntityDriverFromModelAttribute(Driver driver, ModelAttributeDriver modelAttributeDriver) {
        City city = new City();
        city.setCityId(modelAttributeDriver.getCurrentCityFK());

        driver.setCurrentCityFK(city);
        driver.setPersonalNumber(modelAttributeDriver.getPersonalNumber());
        driver.setFirstName(modelAttributeDriver.getFirstName());
        driver.setLastName(modelAttributeDriver.getLastName());

        return driver;
    }

    @Override
    public void assignDriverToTruck(Integer driverId, Integer truckId) throws LogiwebServiceException, LogiwebValidationException {
        try {
            entityManager.getTransaction().begin();
            Driver driver = driverDAO.findById(driverId);
            Truck truck = truckDAO.findById(truckId);

            if(driver == null || truck == null) {
                throw new LogiwebValidationException("Driver and truck must exist.");
            }

            List<Driver> driverCountInTruck = truck.getDriversInTruck();

            if(driverCountInTruck == null) {
                driverCountInTruck = new ArrayList<>();
            }

            if(driverCountInTruck.size() < truck.getDriverCount()) {
                driverCountInTruck.add(driver);
                driver.setCurrentTruckFK(truck);
            } else {
                throw new LogiwebValidationException("All crew positions are occupied. Can't add Driver to crew.");
            }

            driverDAO.update(driver);
            entityManager.getTransaction().commit();

        } catch (LogiwebDAOException e) {
            LOGGER.warn(e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

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
     * @throws LogiwebServiceException if unexpected exception on lower level occurred (not user fault).
     */
    @Override
    @Transactional
    public Integer calculateWorkingHoursForDriver(Integer driverId) throws LogiwebServiceException { //
        try {
            Driver driver = driverDAO.findById(driverId);
            List<DriverShift> shiftRecords = driverShiftDAO.findThisMonthRecordsForDrivers(driver);
            Map<Driver, Integer> workingHours = sumWorkingHoursForThisMonth(shiftRecords);

            //if driver don't have any records yet
            if (workingHours.get(driver) == null) {
                workingHours.put(driver, 0);
            }

            return workingHours.get(driver);

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in DriverServiceImpl - calculateWorkingHoursForDriver().", e);
            throw new LogiwebServiceException(e);
        }
    }

    //***
    @Override
    public void editDriver(Driver editedDriver) throws LogiwebServiceException {

    }

    /** //
     * Find drivers.
     *
     * @return empty list if nothing found.
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault).
     */
    @Override
    @Transactional
    public List<Driver> findAllDrivers() throws LogiwebServiceException { //
        try {
            return driverDAO.findAll();

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in DriverServiceImpl - findAllDrivers().", e);
            throw new LogiwebServiceException(e);
        }
    }

    @Override
    public Driver findDriverById(Integer driverId) throws LogiwebServiceException {
        Driver driverResult;

        try {
            entityManager.getTransaction().begin();
            driverResult = driverDAO.findById(driverId);
            entityManager.getTransaction().commit();

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in DriverServiceImpl - findDriverById().", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }

        return driverResult;
    }

    @Override
    public List<DriverShift> findDriverShiftRecordsForThisMonth(Driver driver) throws LogiwebServiceException {
        List<DriverShift> driverShiftRecords;

        try {
            entityManager.getTransaction().begin();
            driverShiftRecords = driverShiftDAO.findThisMonthRecordsForDrivers(driver);
            entityManager.getTransaction().commit();

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in DriverServiceImpl - findDriverShiftRecordsForThisMonth.", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }

        return driverShiftRecords;
    }

    @Override
    public void deleteDriver(Driver deletedDriver) throws LogiwebServiceException, LogiwebValidationException {
        try {
            entityManager.getTransaction().begin();
            Driver existingDriverToDelete = driverDAO.findById(deletedDriver.getDriverId());

            if (existingDriverToDelete == null) {
                throw new LogiwebValidationException("Driver with driver ID#" + deletedDriver.getDriverId() + " not found.");
            }

            if (existingDriverToDelete.getCurrentTruckFK() != null) {
                throw new LogiwebValidationException("Driver is assigned to truck. Removal is not possible.");
            }

            driverDAO.delete(existingDriverToDelete);
            entityManager.getTransaction().commit();

            LOGGER.info("Driver with personal number #" + existingDriverToDelete.getPersonalNumber()
                    + " " + existingDriverToDelete.getFirstName() + " " + existingDriverToDelete.getLastName() + " removed");

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in DriverServiceImpl - deleteDriver().", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public void startShiftForDriver(Integer driverId) throws LogiwebServiceException, LogiwebValidationException {
        try {
            entityManager.getTransaction().begin();
            Driver driver = driverDAO.findById(driverId);
            if (driver == null) {
                throw new LogiwebValidationException("Provide valid driver employee id.");
            }
            if (driver.getDriverStatus() != DriverStatus.FREE) {
                throw new LogiwebValidationException("Driver must be free to start new shift.");
            }

            DriverShift newShift = new DriverShift();
            newShift.setDriverForThisShiftFK(driver);
            newShift.setDriverShiftBegin(new Date()); // now

            driverShiftDAO.create(newShift);

            driver.setDriverStatus(DriverStatus.DRIVING);
            driverDAO.update(driver);
            entityManager.getTransaction().commit();

        } catch (LogiwebDAOException e) {
            LOGGER.warn(e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public Set<Driver> findUnassignedDriversByWorkingHoursAndCity(City city, Double maxWorkingHours) throws LogiwebServiceException {
        try {
            entityManager.getTransaction().begin();

            List<Driver> freeDriversInCity = driverDAO.findByCityWhereNotAssignedToTruck(city);
            List<DriverShift> shiftRecords = driverShiftDAO.findThisMonthRecordsForDrivers(freeDriversInCity);

            entityManager.getTransaction().commit();

            Map<Driver, Integer> workingHours = sumWorkingHoursForThisMonth(shiftRecords);

            for (Driver driver : freeDriversInCity) {   //add drivers that don't yet have journals
                if(workingHours.get(driver) == null) {
                    workingHours.put(driver, 0);
                }
            }

            filterDriversByMaxWorkingHours(workingHours, maxWorkingHours);

            return workingHours.keySet();

        } catch (LogiwebDAOException e) {
            LOGGER.warn(e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    /**
     * Calculate total working hours for drivers that are listed in shift records.
     *
     * @param shiftRecords
     * @return Map with driver as keys and working hours as values.
     */
    private Map<Driver, Integer> sumWorkingHoursForThisMonth(Collection<DriverShift> shiftRecords) { //
        Map<Driver, Integer> workingHoursForDrivers = new HashMap<>();

        Date firstDayOfCurrentMonth = DateUtil.getFirstDateOfCurrentMonth();
        Date firstDayOfNextMonth = DateUtil.getFirstDayOfNextMonth();

        for (DriverShift ds : shiftRecords) {
            Driver driver = ds.getDriverForThisShiftFK();
            Date shiftBeggined = ds.getDriverShiftBegin();
            Date shiftEnded = ds.getDriverShiftEnd();

            /*
             * If shift ended after or before current month
             * we trim it by limiting shift end to first day of next
             * month (00:00:00) and  shift start to first day of month (00:00:00).
             * if shift is not yet ended -- set Now as upper limit.            *
             */
            if (shiftEnded == null) {
                shiftEnded = new Date();
            }
            if (shiftBeggined.getTime() < firstDayOfCurrentMonth.getTime()) {
                shiftBeggined = firstDayOfCurrentMonth;
            }
            if (shiftEnded.getTime() > firstDayOfNextMonth.getTime()) {
                shiftEnded = firstDayOfNextMonth;
            }

            Integer shiftDuration = DateUtil.diffInHours(shiftBeggined, shiftEnded);
            Integer totalHoursForDriver = workingHoursForDrivers.get(driver);

            if(totalHoursForDriver == null) {
                workingHoursForDrivers.put(driver, shiftDuration);
            } else {
                workingHoursForDrivers.put(driver, totalHoursForDriver + shiftDuration);
            }
        }

        return workingHoursForDrivers;
    }

    private void filterDriversByMaxWorkingHours(Map<Driver, Integer> workingHoursToFilter, Double maxWorkingHours) {
        for (Map.Entry<Driver, Integer> entry : workingHoursToFilter.entrySet()) {
            if (entry.getValue() > maxWorkingHours) {
                workingHoursToFilter.remove(entry.getKey());
            }
        }
    }


}
