package ru.tsystems.javaschool.kuzmenkov.logiweb.services.Impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.DriverDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.DriverShiftDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.TruckDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.DriverShift;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.DriverStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.DateUtil;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebValidator;

import javax.persistence.EntityManager;
import java.util.*;

/**
 * Data manipulation and business logic related to drivers.
 *
 * @author Nikolay Kuzmenkov.
 */
@Service
public class DriverServiceImpl implements DriverService {

    private static final Logger LOGGER = Logger.getLogger(DriverServiceImpl.class);

    private EntityManager entityManager;
    private DriverDAO driverDAO;
    private TruckDAO truckDAO;
    private DriverShiftDAO driverShiftDAO;

    public DriverServiceImpl(DriverDAO driverDAO, DriverShiftDAO driverShiftDAO, TruckDAO truckDAO, EntityManager entityManager) {
        this.driverDAO = driverDAO;
        this.driverShiftDAO = driverShiftDAO;
        this.entityManager = entityManager;
        this.truckDAO = truckDAO;
    }

    @Override
    public Driver addNewDriver(Driver newDriver) throws LogiwebServiceException, LogiwebValidationException {
        LogiwebValidator.validateDriverFormValues(newDriver);

        try {
            newDriver.setDriverStatus(DriverStatus.REST);

            entityManager.getTransaction().begin();
            Driver driverWithSamePersonalNumber = driverDAO.findDriverByPersonalNumber(newDriver.getPersonalNumber());

            if (driverWithSamePersonalNumber != null) {
                throw new LogiwebValidationException("Driver with this personal number #" + newDriver.getPersonalNumber()
                        + " is already exist.");
            }

            driverDAO.create(newDriver);
            entityManager.getTransaction().commit();

            LOGGER.info("Driver created: " + newDriver.getFirstName() + " " + newDriver.getLastName()
                    + " personal number #" + newDriver.getPersonalNumber() + " , ID: " + newDriver.getDriverId());

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in DriverServiceImpl - addNewDriver().", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }

        return newDriver;
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

    @Override
    @Transactional
    public Integer calculateWorkingHoursForDriver(Integer driverId) throws LogiwebServiceException {
        Integer workingHoursResult;

        try {
            List<DriverShift> shiftRecords = driverShiftDAO.findThisMonthRecordsForDriver(driverId);
            Map<Driver, Integer> workingHours = sumWorkingHoursForThisMonth(shiftRecords);

            //if driver don't have any records yet
            if (workingHours.get(driver) == null) {
                workingHours.put(driver, 0);
            }

            workingHoursResult = workingHours.get(driver);

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in DriverServiceImpl - calculateWorkingHoursForDriver().", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }

        return workingHoursResult;
    }

    //***
    @Override
    public void editDriver(Driver editedDriver) throws LogiwebServiceException {

    }

    @Override
    public List<Driver> findAllDrivers() throws LogiwebServiceException {
        List<Driver> allDriversResult;

        try {
            entityManager.getTransaction().begin();
            allDriversResult = driverDAO.findAll();
            entityManager.getTransaction().commit();

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in DriverServiceImpl - findAllDrivers().", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }

        return allDriversResult;
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
            driverShiftRecords = driverShiftDAO.findThisMonthRecordsForDriver(driver);
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
            if (driver.getDriverStatus() != DriverStatus.REST) {
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

    private Map<Driver, Integer> sumWorkingHoursForThisMonth(Collection<DriverShift> shiftRecords) {
        Map<Driver, Integer> workingHoursForDrivers = new HashMap<Driver, Integer>();

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
