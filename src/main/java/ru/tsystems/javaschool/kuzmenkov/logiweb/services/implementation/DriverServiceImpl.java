package ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.kuzmenkov.logiweb.controllers.model.ModelAttributeDriver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.*;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.*;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.DriverStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.Role;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.FreightService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.UserService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.DateUtil;

import java.util.*;

/**
 * Data manipulation and business logic related to drivers.
 *
 * @author Nikolay Kuzmenkov.
 */
@Service("driverService")
public class DriverServiceImpl implements DriverService {

    private static final Logger LOGGER = Logger.getLogger(DriverServiceImpl.class);

    @Autowired
    private FreightService freightService;
    @Autowired
    UserService userService;
    @Autowired
    private DriverDAO driverDAO;
    @Autowired
    private TruckDAO truckDAO;
    @Autowired
    private DriverShiftDAO driverShiftDAO;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private UserDAO userDAO;

    public DriverServiceImpl() {

    }

    public DriverServiceImpl(DriverDAO driverDAO, TruckDAO truckDAO) {
        this.driverDAO = driverDAO;
        this.truckDAO = truckDAO;
    }

    @Override
    @Transactional
    public Driver getDriverByPersonalNumber(Integer personalNumber) throws LogiwebServiceException {
        try {
            return driverDAO.findDriverByPersonalNumber(personalNumber);

        } catch (LogiwebDAOException e) {
           throw new LogiwebServiceException(e);
        }
    }

    @Override
    @Transactional
    public Integer addNewDriver(ModelAttributeDriver driverFromForm) throws LogiwebServiceException, //
            LogiwebValidationException {
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

    private Driver createEntityDriverFromModelAttribute(Driver driver, ModelAttributeDriver modelAttributeDriver) { //
        City city = new City();
        city.setCityId(modelAttributeDriver.getCurrentCityFK());

        driver.setCurrentCityFK(city);
        driver.setPersonalNumber(modelAttributeDriver.getPersonalNumber());
        driver.setFirstName(modelAttributeDriver.getFirstName());
        driver.setLastName(modelAttributeDriver.getLastName());

        return driver;
    }

    @Override //
    @Transactional
    public void assignDriverToTruck(Integer driverId, Integer truckId) throws LogiwebServiceException, LogiwebValidationException {
        try {
            Driver driver = driverDAO.findById(driverId);
            Truck truck = truckDAO.findById(truckId);

            if(driver == null || truck == null) {
                throw new LogiwebValidationException("Driver and truck must exist.");
            }

            Set<Driver> driverCountInTruck = truck.getDriversInTruck();

            if(driverCountInTruck == null) {
                driverCountInTruck = new HashSet<>();
            }

            if(driverCountInTruck.size() < truck.getDriverCount()) {
                driverCountInTruck.add(driver);
                driver.setCurrentTruckFK(truck);
            } else {
                throw new LogiwebValidationException("All crew positions are occupied. Can't add Driver to crew.");
            }

            driverDAO.update(driver);

        } catch (LogiwebDAOException e) {
            LOGGER.warn(e);
            throw new LogiwebServiceException(e);
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
    public Float calculateWorkingHoursForDriver(Integer driverId) throws LogiwebServiceException { //
        try {
            Driver driver = driverDAO.findById(driverId);
            List<DriverShift> shiftRecords = driverShiftDAO.findThisMonthRecordsForDriver(driver);
            Map<Driver, Float> workingHours = sumWorkingHoursForThisMonth(shiftRecords);

            //if driver don't have any records yet
            if (workingHours.get(driver) == null) {
                workingHours.put(driver, 0f);
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

    @Override //
    @Transactional
    public Driver findDriverById(Integer driverId) throws LogiwebServiceException {
        try {
            return driverDAO.findById(driverId);

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Something unexcpected happend.");
            throw new LogiwebServiceException(e);
        }
    }

    @Override //
    @Transactional
    public List<DriverShift> findDriverShiftRecordsForThisMonth(Integer driverId) throws LogiwebServiceException {
        try {
            Driver driver = driverDAO.findById(driverId);

            if (driver == null) {
                return new ArrayList<>();
            } else {
                return driverShiftDAO.findThisMonthRecordsForDriver(driver);
            }

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in DriverServiceImpl - findDriverShiftRecordsForThisMonth.", e);
            throw new LogiwebServiceException(e);
        }
    }

    @Override
    @Transactional
    public void deleteDriver(Integer driverId) throws LogiwebServiceException, LogiwebValidationException { //
        try {
            Driver existingDriverToDelete = driverDAO.findById(driverId);

            if (existingDriverToDelete == null) {
                throw new LogiwebValidationException("Driver with driver ID#" + driverId + " not found.");
            }

            if (existingDriverToDelete.getCurrentTruckFK() != null) {
                throw new LogiwebValidationException("Driver is assigned to truck. Removal is not possible.");
            }

            User driverAccountToDelete = existingDriverToDelete.getLogiwebDriverAccount();
            driverDAO.delete(existingDriverToDelete);
            userDAO.delete(driverAccountToDelete);

            LOGGER.info("Driver with personal number #" + existingDriverToDelete.getPersonalNumber()
                    + " " + existingDriverToDelete.getFirstName() + " " + existingDriverToDelete.getLastName() + " removed");

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in DriverServiceImpl - deleteDriver().", e);
            throw new LogiwebServiceException(e);
        }
    }

    @Override
    @Transactional
    public void startShiftForDriver(Integer driverNumber) throws LogiwebServiceException, LogiwebValidationException {
        try {
            Driver driver = driverDAO.findDriverByPersonalNumber(driverNumber);

            if (driver == null) {
                throw new LogiwebValidationException("Provide valid driver personal number.");
            }

            if (driver.getDriverStatus() != DriverStatus.FREE) {
                throw new LogiwebValidationException("Driver must be free to start new shift.");
            }

            DriverShift unfinishedShift = driverShiftDAO.findUnfinishedShiftForDriver(driver);

            if (unfinishedShift != null) {
                throw new LogiwebValidationException("Finish existing shift before creating new one.");
            }

            DriverShift newShift = new DriverShift();
            newShift.setDriverForThisShiftFK(driver);
            newShift.setDriverShiftBegin(new Date()); // now

            driverShiftDAO.create(newShift);

            driver.setDriverStatus(DriverStatus.REST_IN_SHIFT);
            driverDAO.update(driver);

        } catch(LogiwebDAOException e) {
            LOGGER.warn(e);
            throw new LogiwebServiceException(e);
        }
    }

    @Override
    @Transactional
    public void endShiftForDriver(Integer driverNumber) throws LogiwebValidationException, LogiwebServiceException {
        try {
            Driver driver = driverDAO.findDriverByPersonalNumber(driverNumber);

            if (driver == null) {
                throw new LogiwebValidationException("Provide valid driver employee id.");
            }

            DriverShift unfinishedShift = driverShiftDAO.findUnfinishedShiftForDriver(driver);

            if (unfinishedShift == null) {
                throw new LogiwebValidationException("There is no active shift for this driver.");
            }

            unfinishedShift.setDriverShiftEnd(new Date());
            driverShiftDAO.update(unfinishedShift);

            driver.setDriverStatus(DriverStatus.FREE);
            driverDAO.update(driver);

        } catch(LogiwebDAOException e) {
            LOGGER.warn(e);
            throw new LogiwebServiceException(e);
        }
    }

    @Override
    @Transactional
    public void setStatusDrivingForDriver(Integer driverNumber) throws LogiwebServiceException {
        try {
            Driver driver = driverDAO.findDriverByPersonalNumber(driverNumber);

            if (driver == null) {
                throw new LogiwebServiceException();
            }

            driver.setDriverStatus(DriverStatus.DRIVING);
            driverDAO.update(driver);

        } catch (LogiwebDAOException e) {
            LOGGER.warn(e);
            throw new LogiwebServiceException(e);
        }
    }

    @Override //
    @Transactional
    public Driver getDriverWithFullInfo(Integer driverId) throws LogiwebServiceException {
        try {
            Driver driver = driverDAO.findById(driverId);
            if (driver == null) {
                return null;
            }

            driver.setWorkingHoursThisMonth(calculateWorkingHoursForDriver(driver.getDriverId()));
            driver.setDriverShiftRecords(findDriverShiftRecordsForThisMonth(driver.getDriverId()));

            if (driver.getCurrentTruckFK() == null) {
                return driver;
            }

            Order order = orderDAO.findById(driver.getCurrentTruckFK().getOrderForThisTruck().getOrderId());
            OrderRoute orderRouteInfo = freightService.getRouteInformationForOrder(order.getOrderId());
            driver.setOrderRouteInfoForThisDriver(orderRouteInfo);

            return driver;

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in DriverServiceImpl - getDriverWithFullInfo().", e);
            throw new LogiwebServiceException(e);
        }
    }

    @Override
    @Transactional //
    public Set<Driver> findUnassignedDriversByWorkingHoursAndCity(City city, Float maxWorkingHours)
            throws LogiwebServiceException {
        try {
            List<Driver> freeDriversInCity = driverDAO.findByCityWhereNotAssignedToTruck(city);
            List<DriverShift> shiftRecords = driverShiftDAO.findThisMonthRecordsForDrivers(freeDriversInCity);

            Map<Driver, Float> workingHours = sumWorkingHoursForThisMonth(shiftRecords);

            for (Driver driver : freeDriversInCity) {   //add drivers that don't yet have journals
                if(workingHours.get(driver) == null) {
                    workingHours.put(driver, 0f);
                }
            }

            filterDriversByMaxWorkingHours(workingHours, maxWorkingHours);

            return workingHours.keySet();

        } catch (LogiwebDAOException e) {
            LOGGER.warn(e);
            throw new LogiwebServiceException(e);
        }
    }

    /** //
     *
     * Calculate total working hours for drivers that are listed in shift records.
     *
     * @param shiftRecords
     * @return Map with driver as keys and working hours as values.
     */
    private Map<Driver, Float> sumWorkingHoursForThisMonth(Collection<DriverShift> shiftRecords) {
        Map<Driver, Float> workingHoursForDrivers = new HashMap<>();

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

            Float shiftDuration = DateUtil.diffInHours(shiftBeggined, shiftEnded);
            Float totalHoursForDriver = workingHoursForDrivers.get(driver);

            if(totalHoursForDriver == null) {
                workingHoursForDrivers.put(driver, shiftDuration);
            } else {
                workingHoursForDrivers.put(driver, totalHoursForDriver + shiftDuration);
            }
        }

        return workingHoursForDrivers;
    }

    /**
     * Filter Map of working hours records.
     * Delete entry if limit of hours is exceeded.
     *
     * @param workingHoursToFilter
     * @param maxWorkingHours
     */
    private void filterDriversByMaxWorkingHours(Map<Driver, Float> workingHoursToFilter, Float maxWorkingHours) {
        Iterator<Map.Entry<Driver, Float>> it = workingHoursToFilter.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry<Driver, Float> entry = it.next();

            if(entry.getValue() > maxWorkingHours) {
                it.remove();
            }
        }
    }
}
