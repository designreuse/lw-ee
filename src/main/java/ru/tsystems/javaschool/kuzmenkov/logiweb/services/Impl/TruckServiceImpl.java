package ru.tsystems.javaschool.kuzmenkov.logiweb.services.Impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.DriverDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.TruckDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.OrderStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.TruckStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.TruckService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebValidator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Business logic related to trucks (implementation).
 *
 * @author Nikolay Kuzmenkov.
 */
@Service("truckService")
public class TruckServiceImpl implements TruckService {

    private static final Logger LOGGER = Logger.getLogger(TruckServiceImpl.class);

    @Autowired
    private DriverDAO driverDAO;
    @Autowired
    private TruckDAO truckDAO;

    /**
     * Add new truck.
     *
     * @param newTruck
     * @return same truck.
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault).
     * @throws LogiwebValidationException if truck don't have all required fields or not unique truck number.
     */
    @Override //
    @Transactional
    public Integer addNewTruck(Truck newTruck) throws LogiwebServiceException, LogiwebValidationException {
        try {
            newTruck.setTruckStatus(TruckStatus.WORKING);
            City city = new City();
            city.setCityId(newTruck.getCurrentCityId());
            newTruck.setCurrentCityFK(city);

            LogiwebValidator.validateTruckFormValues(newTruck);
            Truck truckWithTruckNumber = truckDAO.findTruckByTruckNumber(newTruck.getTruckNumber());

            if (truckWithTruckNumber != null) {
                throw new LogiwebValidationException("Truck with number #" + newTruck.getTruckNumber() +
                        " is already exist.");
            }

            if (!LogiwebValidator.validateTruckNumber(newTruck.getTruckNumber())) {
                throw new LogiwebValidationException("Truck number #" + newTruck.getTruckNumber() + " is not valid.");
            }

            truckDAO.create(newTruck);

            LOGGER.info("Truck created: truck number #" + newTruck.getTruckNumber() + " ID: " + newTruck.getTruckId());

            return newTruck.getTruckId();

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in TruckServiceImpl - addNewTruck().", e);
            throw new LogiwebServiceException(e);
        }
    }

    /**
     * Find all trucks.
     *
     * @return list of trucks or empty list if nothing found.
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault).
     */
    @Override //
    @Transactional
    public List<Truck> findAllTrucks() throws LogiwebServiceException {
        try {
            return truckDAO.findAll();

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in TruckServiceImpl - findAllTrucks().", e);
            throw new LogiwebServiceException(e);
        }
    }

    @Override //
    @Transactional
    public List<Truck> findFreeAndUnbrokenByFreightCapacity(Float minFreightWeightCapacity) throws LogiwebServiceException {
        try {
            return truckDAO.findByMinCapacityWhereStatusOkAndNotAssignedToOrder(minFreightWeightCapacity);

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        }
    }

    /**
     * Find truck by truck ID.
     *
     * @param truckId
     * @return truck by this truck ID or null.
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault).
     */
    @Override
    @Transactional
    public Truck findTruckById(Integer truckId) throws LogiwebServiceException {
        Truck truckResult;

        try {

            truckResult = truckDAO.findById(truckId);


        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in TruckServiceImpl - findTruckById().", e);
            throw new LogiwebServiceException(e);
        }
        return truckResult;
    }

    @Override
    @Transactional
    public void removeAssignedOrderAndDriversFromTruck(Truck truck) throws LogiwebServiceException, LogiwebValidationException {
        if (truck.getOrderForThisTruck().getOrderStatus() == OrderStatus.READY_TO_GO) {
            throw new LogiwebValidationException("Can't remove truck from READY TO GO order.");
        }

        try {

            Order order = truck.getOrderForThisTruck();
            List<Driver> drivers = truck.getDriversInTruck();
            truck.setOrderForThisTruck(null);
            truck.setDriversInTruck(new ArrayList<Driver>());

            truckDAO.update(truck);

            for(Driver driver : drivers) {
                driver.setCurrentTruckFK(null);
                driverDAO.update(driver);
            }

            if(order != null) {
                order.setAssignedTruckFK(null);
            }

            LOGGER.info("Truck id#" + truck.getTruckId() + " and its drivers removed from order.");

        } catch (Exception e) {
            LOGGER.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        }
    }

    @Override //
    @Transactional
    public void removeTruck(Integer truckId) throws LogiwebValidationException, LogiwebServiceException {
        try {
            Truck truckToRemove = truckDAO.findById(truckId);

            if (truckToRemove == null) {
                throw new LogiwebValidationException("Truck " + truckId + " not exist. Deletion forbiden.");
            }
            else if (truckToRemove.getOrderForThisTruck() != null) {
                throw new LogiwebValidationException("Truck is assigned to order. Deletion forbiden.");
            }
            else if (!truckToRemove.getDriversInTruck().isEmpty()) {
                throw new LogiwebValidationException("Truck is assigned to one or more drivers. Deletion forbiden.");
            }

            truckDAO.delete(truckToRemove);

            LOGGER.info("Truck removed. Plate " + truckToRemove.getTruckNumber() + " ID: " + truckToRemove.getTruckId());

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        }
    }
}
