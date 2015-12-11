package ru.tsystems.javaschool.kuzmenkov.logiweb.services.Impl;

import org.apache.log4j.Logger;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.DriverDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.TruckDAO;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Business logic related to trucks (implementation).
 *
 * @author Nikolay Kuzmenkov.
 */
public class TruckServiceImpl implements TruckService {

    private static final Logger LOGGER = Logger.getLogger(TruckServiceImpl.class);

    private EntityManager entityManager;

    private DriverDAO driverDAO;
    private TruckDAO truckDAO;

    public TruckServiceImpl(DriverDAO driverDAO, TruckDAO truckDAO, EntityManager entityManager) {
        this.driverDAO = driverDAO;
        this.truckDAO = truckDAO;
        this.entityManager = entityManager;
    }

    /**
     * Add new truck.
     *
     * @param newTruck
     * @return same truck.
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault).
     * @throws LogiwebValidationException if truck don't have all required fields or not unique truck number.
     */

    @Override
    public Truck addNewTruck(Truck newTruck) throws LogiwebServiceException, LogiwebValidationException {
        LogiwebValidator.validateTruckFormValues(newTruck);

        if (!LogiwebValidator.validateTruckNumber(newTruck.getTruckNumber())) {
            throw new LogiwebValidationException("Truck number #" + newTruck.getTruckNumber() + " is not valid.");
        }

        try {
            newTruck.setTruckStatus(TruckStatus.WORKING);

            entityManager.getTransaction().begin();
            Truck truckWithTruckNumber = truckDAO.findTruckByTruckNumber(newTruck.getTruckNumber());

            if (truckWithTruckNumber != null) {
                throw new LogiwebValidationException("Truck with number #" + newTruck.getTruckNumber() + " is already exist.");
            }

            truckDAO.create(newTruck);
            entityManager.getTransaction().commit();

            LOGGER.info("Truck created: truck number #" + newTruck.getTruckNumber() + " ID: " + newTruck.getTruckId());


        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in TruckServiceImpl - addNewTruck().", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }

        return newTruck;
    }

    /**
     * Find all trucks.
     *
     * @return list of trucks or empty list if nothing found.
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault).
     */
    @Override
    public List<Truck> findAllTrucks() throws LogiwebServiceException {
        List<Truck> allTrucksResult;

        try {
            entityManager.getTransaction().begin();
            allTrucksResult = truckDAO.findAll();
            entityManager.getTransaction().commit();

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in TruckServiceImpl - findAllTrucks().", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }

        return allTrucksResult;
    }

    @Override
    public List<Truck> findFreeAndUnbrokenByCargoCapacity(Float minCargoWeightCapacity) throws LogiwebServiceException {
        List<Truck> freeTrucksResult;

        try {
            entityManager.getTransaction().begin();
            freeTrucksResult = truckDAO.findByMinCapacityWhereStatusOkAndNotAssignedToOrder(minCargoWeightCapacity);
            entityManager.getTransaction().commit();

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }

        return freeTrucksResult;
    }

    /**
     * Find truck by truck ID.
     *
     * @param truckId
     * @return truck by this truck ID or null.
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault).
     */
    @Override
    public Truck findTruckById(Integer truckId) throws LogiwebServiceException {
        Truck truckResult;

        try {
            entityManager.getTransaction().begin();
            truckResult = truckDAO.findById(truckId);
            entityManager.getTransaction().commit();

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in TruckServiceImpl - findTruckById().", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }

        return truckResult;
    }

    @Override
    public void removeAssignedOrderAndDriversFromTruck(Truck truck) throws LogiwebServiceException, LogiwebValidationException {
        if (truck.getOrderForThisTruck().getOrderStatus() == OrderStatus.READY_TO_GO) {
            throw new LogiwebValidationException("Can't remove truck from READY TO GO order.");
        }

        try {
            entityManager.getTransaction().begin();

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

            entityManager.getTransaction().commit();

            LOGGER.info("Truck id#" + truck.getTruckId() + " and its drivers removed from order.");

        } catch (Exception e) {
            LOGGER.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public void removeTruck(Truck truckToRemove) throws LogiwebValidationException, LogiwebServiceException {
        Integer truckToRemoveId = truckToRemove.getTruckId();

        try {
            entityManager.getTransaction().begin();
            truckToRemove = truckDAO.findById(truckToRemove.getTruckId());

            if (truckToRemove == null) {
                throw new LogiwebValidationException("Truck " + truckToRemoveId + " not exist. Deletion forbiden.");
            }
            else if (truckToRemove.getOrderForThisTruck() != null) {
                throw new LogiwebValidationException("Truck is assigned to order. Deletion forbiden.");
            }
            else if (!truckToRemove.getDriversInTruck().isEmpty()) {
                throw new LogiwebValidationException("Truck is assigned to one or more drivers. Deletion forbiden.");
            }

            truckDAO.delete(truckToRemove);
            entityManager.getTransaction().commit();

            LOGGER.info("Truck removed. Plate " + truckToRemove.getTruckNumber() + " ID: " + truckToRemove.getTruckId());

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }
}
