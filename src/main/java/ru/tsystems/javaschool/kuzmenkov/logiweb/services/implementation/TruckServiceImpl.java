package ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.TruckDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.TruckDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.DriverStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.OrderStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.TruckStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.TruckService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.EntityDTODataConverter;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Business logic related to trucks (implementation).
 *
 * @author Nikolay Kuzmenkov.
 */
@Service("truckService")
public class TruckServiceImpl implements TruckService {

    private static final Logger LOGGER = Logger.getLogger(TruckServiceImpl.class);

    @Autowired
    private EntityDTODataConverter converter;
    @Autowired
    private TruckDAO truckDAO;

    public boolean validateTruckNumber(String truckNumber) {
        if(!truckNumber.matches("^[A-Z0-9]{7}$")) {             // only letters and numbers (7 times)
            return false;
        }
        else if(!truckNumber.matches("^[A-Z]{2}\\d{5}$")) {
            return false;
        }
        else {
            return true;
        }
    }


    /**
     * Add new truck.
     *
     * @param newTruckDTO
     * @return same truck.
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault).
     * @throws LogiwebValidationException if truck don't have all required fields or not unique truck number.
     */
    @Override //
    @Transactional
    public Integer addNewTruck(TruckDTO newTruckDTO) throws LogiwebServiceException {
        try {
            if (!LogiwebValidator.validateTruckNumber(newTruckDTO.getTruckNumber())) {
                throw new LogiwebValidationException("Truck number #" + newTruckDTO.getTruckNumber() + " is not valid.");
            }

            Truck truckWithSameTruckNumber = truckDAO.findTruckByTruckNumber(newTruckDTO.getTruckNumber());

            if (truckWithSameTruckNumber != null) {
                throw new LogiwebValidationException("Truck with number #" + newTruckDTO.getTruckNumber() +
                        " is already exist.");
            }

            Truck newTruckEntity = converter.convertTruckDTOToEntity(newTruckDTO);

            newTruckEntity.setTruckStatus(TruckStatus.WORKING);
            LogiwebValidator.validateTruckFormValues(newTruckEntity);

            truckDAO.create(newTruckEntity);

            LOGGER.info("Truck created: truck number #" + newTruckEntity.getTruckNumber() + " ID: " + newTruckEntity.getTruckId());

            return newTruckEntity.getTruckId();

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
    public List<TruckDTO> findFreeAndUnbrokenByFreightCapacity(Float minFreightWeightCapacity) throws LogiwebServiceException {
        try {
            return converter.convertListTruckEntitiesToDTO(
                    truckDAO.findByMinCapacityWhereStatusOkAndNotAssignedToOrder(minFreightWeightCapacity));

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

    @Override //
    @Transactional
    public void removeAssignedOrderAndDriversFromTruck(Integer truckId) throws LogiwebServiceException {
        try {
            Truck truck = truckDAO.findById(truckId);

            if(truck == null) {
                throw new LogiwebValidationException("Truck not found.");
            }

            if(truck.getOrderForThisTruck() == null) {
                throw new LogiwebValidationException("Order is not assigned.");
            }

            if(truck.getOrderForThisTruck().getOrderStatus() == OrderStatus.READY_TO_GO) {
                throw new LogiwebValidationException("Can't remove truck from READY TO GO order.");
            }

            Order orderForTruck = truck.getOrderForThisTruck();
            Set<Driver> driversInTruck = truck.getDriversInTruck();

            truck.setOrderForThisTruck(null);
            truck.setDriversInTruck(new HashSet<>());

            for (Driver driver : driversInTruck) {
                driver.setCurrentTruckFK(null);
                driver.setDriverStatus(DriverStatus.FREE);
            }

            if(orderForTruck != null) {
                orderForTruck.setAssignedTruckFK(null);
            }

            truckDAO.update(truck);
            LOGGER.info("Truck id#" + truck.getTruckId() + " and its drivers removed from order.");

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        }
    }

    @Override
    @Transactional
    public void removeTruck(Integer truckId) throws LogiwebServiceException {
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
