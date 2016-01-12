package ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.*;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.DriverDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.RecordNotFoundException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.EntityDTODataConverter;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.*;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.DriverStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.Role;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.FreightService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.UserService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.DateUtil;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.PasswordConverter;

import java.security.NoSuchAlgorithmException;
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
    private EntityDTODataConverter converter;

    @Autowired
    private FreightService freightService;
    @Autowired
    private UserService userService;

    @Autowired
    private CityDAO cityDAO;
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

    @Override
    @Transactional
    public Driver getDriverByPersonalNumber(Integer personalNumber) throws LogiwebDAOException {
        return driverDAO.findDriverByPersonalNumber(personalNumber);
    }

    @Override
    @Transactional
    public Integer addNewDriver(DriverDTO newDriverDTO) throws LogiwebDAOException, LogiwebValidationException, NoSuchAlgorithmException {
        Driver driverWithSamePersonalNumber = driverDAO.findDriverByPersonalNumber(newDriverDTO.getPersonalNumber());

        if (driverWithSamePersonalNumber != null) {
            throw new LogiwebValidationException("Driver with this personal number #" + newDriverDTO.getPersonalNumber()
                    + " is already exist.");
        }

        String personalNumberAsString = String.valueOf(newDriverDTO.getPersonalNumber());
        String driverAccountEmail = "driver" + personalNumberAsString + "@logiweb.com";
        String driverAccountPassword = PasswordConverter.getMD5Hash("12345");

        Integer newDriverUserId = userService.createNewUser(driverAccountEmail, driverAccountPassword, Role.ROLE_DRIVER);
        User accountForDriver = userDAO.findById(newDriverUserId);

        Driver newDriverEntity = converter.convertDriverDTOToEntity(newDriverDTO);
        newDriverEntity.setLogiwebDriverAccount(accountForDriver);
        newDriverEntity.setDriverStatus(DriverStatus.FREE);

        driverDAO.create(newDriverEntity);

        LOGGER.info("Driver created: " + newDriverEntity.getFirstName() + " " + newDriverEntity.getLastName()
                    + " personal number #" + newDriverEntity.getPersonalNumber() + " , ID: " + newDriverEntity.getDriverId());

        return newDriverEntity.getDriverId();
    }

    @Override
    @Transactional
    public void assignDriverToTruck(Integer driverId, Integer truckId) throws LogiwebDAOException, LogiwebValidationException {
        Driver driver = driverDAO.findById(driverId);
        Truck truck = truckDAO.findById(truckId);

        if(driver == null || truck == null) {
            throw new LogiwebValidationException("Driver and truck must exist.");
        }

        Set<Driver> driverCountInTruck = truck.getDriversInTruck();

        if(driverCountInTruck == null) {
            driverCountInTruck = new HashSet<>();
            truck.setDriversInTruck(driverCountInTruck);
        }

        if(driverCountInTruck.size() < truck.getDriverCount()) {
            driverCountInTruck.add(driver);
            driver.setCurrentTruckFK(truck);
        } else {
            throw new LogiwebValidationException("All crew positions are occupied. Can't add Driver to crew.");
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
     * @throws LogiwebDAOException if unexpected exception on lower level occurred (not user fault).
     */
    @Override
    @Transactional
    public Float calculateWorkingHoursForDriver(Integer driverId) throws LogiwebDAOException {
        Driver driver = driverDAO.findById(driverId);
        List<DriverShift> shiftRecords = driverShiftDAO.findThisMonthRecordsForDriver(driver);
        Map<Driver, Float> workingHours = sumWorkingHoursForThisMonth(shiftRecords);

        //if driver don't have any records yet
        if (workingHours.get(driver) == null) {
            workingHours.put(driver, 0f);
        }

        return workingHours.get(driver);
    }

    @Override
    @Transactional
    public void editDriver(DriverDTO editedDriverDTO) throws LogiwebDAOException, LogiwebValidationException {
        Driver driverWithSameNumber = driverDAO.findDriverByPersonalNumber(editedDriverDTO.getPersonalNumber());

        if (driverWithSameNumber != null && driverWithSameNumber.getDriverId() == editedDriverDTO.getDriverId()) {
            throw new LogiwebValidationException("Driver number  #"
                    + editedDriverDTO.getPersonalNumber() + " is already in use.");
        }

        Driver driverEntityToEdit = driverDAO.findById(editedDriverDTO.getDriverId());

        if(driverEntityToEdit == null) {
            throw new LogiwebValidationException("Driver record not found.");
        }

        populateAllowedDriverFieldsFromDTO(driverEntityToEdit, editedDriverDTO);

        driverDAO.update(driverEntityToEdit);
        User account = driverEntityToEdit.getLogiwebDriverAccount();
        account.setUserEmail("driver" + driverEntityToEdit.getPersonalNumber() + "@logiweb.com");

        LOGGER.info("Driver edited. " + driverEntityToEdit.getFirstName() + " "
                + driverEntityToEdit.getLastName() + " ID#" + driverEntityToEdit.getDriverId());

    }

    /**
     * Find drivers.
     *
     * @return empty list if nothing found.
     * @throws LogiwebDAOException if unexpected exception occurred on lower level (not user fault).
     */
    @Override
    @Transactional
    public Set<DriverDTO> findAllDrivers() throws LogiwebDAOException {
        return converter.convertListDriverEntitiesToDTO(driverDAO.findAll());
    }

    @Override
    @Transactional
    public DriverDTO findDriverById(Integer driverId) throws LogiwebDAOException {
        Driver driver = driverDAO.findById(driverId);

        if (driver != null) {
            return converter.convertDriverEntityToDTO(driver);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public List<DriverShift> findDriverShiftRecordsForThisMonth(Integer driverId) throws LogiwebDAOException {
        Driver driver = driverDAO.findById(driverId);

        if (driver == null) {
            return new ArrayList<>();
        } else {
            return driverShiftDAO.findThisMonthRecordsForDriver(driver);
        }
    }

    @Override
    @Transactional
    public void deleteDriver(Integer driverId) throws LogiwebDAOException, LogiwebValidationException {
        Driver existingDriverToDelete = driverDAO.findById(driverId);
        User driverAccountToDelete = existingDriverToDelete.getLogiwebDriverAccount();
        driverDAO.delete(existingDriverToDelete);
        userDAO.delete(driverAccountToDelete);

        LOGGER.info("Driver with personal number #" + existingDriverToDelete.getPersonalNumber()
                + " " + existingDriverToDelete.getFirstName() + " " + existingDriverToDelete.getLastName() + " removed");
    }

    @Override
    @Transactional
    public void startShiftForDriver(Integer driverNumber) throws LogiwebDAOException, LogiwebValidationException {
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
    }

    @Override
    @Transactional
    public void endShiftForDriver(Integer driverNumber) throws LogiwebDAOException, LogiwebValidationException {
        Driver driver = driverDAO.findDriverByPersonalNumber(driverNumber);

        if (driver == null) {
            throw new LogiwebValidationException("Provide valid driver personal number.");
        }

        DriverShift unfinishedShift = driverShiftDAO.findUnfinishedShiftForDriver(driver);

        if (unfinishedShift == null) {
            throw new LogiwebValidationException("There is no active shift for this driver.");
        }

        unfinishedShift.setDriverShiftEnd(new Date());
        driverShiftDAO.update(unfinishedShift);

        driver.setDriverStatus(DriverStatus.FREE);
        driverDAO.update(driver);
    }

    @Override
    @Transactional
    public void setStatusDrivingForDriver(Integer driverNumber) throws LogiwebDAOException {
        Driver driver = driverDAO.findDriverByPersonalNumber(driverNumber);

        if (driver == null) {
            throw new RecordNotFoundException();
        }

        driver.setDriverStatus(DriverStatus.DRIVING);
        driverDAO.update(driver);
    }

    @Override
    @Transactional
    public void setStatusRestingForDriver(Integer driverNumber) throws LogiwebDAOException {
        Driver driver = driverDAO.findDriverByPersonalNumber(driverNumber);

        if (driver == null) {
            throw new RecordNotFoundException();
        }

        driver.setDriverStatus(DriverStatus.REST_IN_SHIFT);
        driverDAO.update(driver)     ;
    }

    @Override
    @Transactional
    public DriverDTO getDriverWithFullInfo(Integer driverId) throws LogiwebDAOException {
        DriverDTO driverDTO = findDriverById(driverId);
        if (driverDTO == null) {
            return null;
        }

        driverDTO.setWorkingHoursThisMonth(calculateWorkingHoursForDriver(driverDTO.getDriverId()));
        driverDTO.setDriverShiftRecordsForThisMonth(findDriverShiftRecordsForThisMonth(driverDTO.getDriverId()));

        if (driverDTO.getCurrentOrderId() == null) {
            return driverDTO;
        }

        Order order = orderDAO.findById(driverDTO.getCurrentOrderId());
        OrderRoute orderRouteInfo = freightService.getRouteInformationForOrder(order.getOrderId());
        driverDTO.setOrderRouteInfoForDriver(orderRouteInfo);

        return driverDTO;
    }

    @Override
    @Transactional
    public Set<DriverDTO> findUnassignedDriversByWorkingHoursAndCity(Integer cityId, Float maxWorkingHours)
            throws LogiwebDAOException {
        City city = cityDAO.findById(cityId);

        if (city == null) {
            throw new RecordNotFoundException();
        }

        List<Driver> freeDriversInCity = driverDAO.findByCityWhereNotAssignedToTruck(city);
        List<DriverShift> shiftRecords = driverShiftDAO.findThisMonthRecordsForDrivers(freeDriversInCity);

        Map<Driver, Float> workingHours = sumWorkingHoursForThisMonth(shiftRecords);

        for (Driver driver : freeDriversInCity) {   //add drivers that don't yet have journals
            if(workingHours.get(driver) == null) {
                workingHours.put(driver, 0f);
            }
        }

        filterDriversByMaxWorkingHours(workingHours, maxWorkingHours);

        return converter.convertListDriverEntitiesToDTO(workingHours.keySet());
    }

    /**
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

        while (it.hasNext()) {
            Map.Entry<Driver, Float> entry = it.next();

            if (entry.getValue() > maxWorkingHours) {
                it.remove();
            }
        }
    }

    /**
     * Populate fields that are used to edit or create new driver.
     * (city, name, surname, employee id, status)
     * @param driverToEdit
     * @param source DriverModel
     * @return
     */
    private Driver populateAllowedDriverFieldsFromDTO(Driver driverToEdit, DriverDTO source) {
        City city = new City();
        city.setCityId(source.getCurrentCityId());
        driverToEdit.setCurrentCityFK(city);

        driverToEdit.setPersonalNumber(source.getPersonalNumber());
        driverToEdit.setFirstName(source.getFirstName());
        driverToEdit.setLastName(source.getLastName());

        return driverToEdit;
    }
}
