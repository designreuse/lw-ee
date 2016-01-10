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

    @Override
    @Transactional
    public void editTruck(TruckDTO editedTruckDTO) throws LogiwebServiceException {
        if(editedTruckDTO.getTruckId() == null || editedTruckDTO.getTruckId() <= 0) {
            throw new LogiwebValidationException("Truck ID is not provided or incorrect.");
        }

        if (!LogiwebValidator.validateTruckNumber(editedTruckDTO.getTruckNumber())) {
            throw new LogiwebValidationException("Truck number " + editedTruckDTO.getTruckNumber() + " is not valid.");
        }

        try {
            Truck truckWithSameNumber = truckDAO.findTruckByTruckNumber(editedTruckDTO.getTruckNumber());

            if (truckWithSameNumber != null && truckWithSameNumber.getTruckId() != editedTruckDTO.getTruckId()) {
                throw new LogiwebValidationException("Truck number " + editedTruckDTO.getTruckNumber() + " is already in use.");
            }

            Truck truckEntityToEdit = truckDAO.findById(editedTruckDTO.getTruckId());

            if (truckEntityToEdit == null) {
                throw new LogiwebValidationException("Truck with ID " + editedTruckDTO.getTruckId() + " not found.");
            }

            if (truckEntityToEdit.getOrderForThisTruck() != null) {
                throw new LogiwebValidationException("Can't edit truck while order is assigned.");
            }

            truckEntityToEdit.setTruckNumber(editedTruckDTO.getTruckNumber());
            truckEntityToEdit.setDriverCount(editedTruckDTO.getDriverCount());
            truckEntityToEdit.setCapacity(editedTruckDTO.getCapacity());
            truckEntityToEdit.setTruckStatus(editedTruckDTO.getTruckStatus());
            City city = new City();
            city.setCityId(editedTruckDTO.getCurrentCityId());
            truckEntityToEdit.setCurrentCityFK(city);
            LogiwebValidator.validateTruckFormValues(truckEntityToEdit);

            truckDAO.update(truckEntityToEdit);

            LOGGER.info("Truck edited. Number " + truckEntityToEdit.getTruckNumber() + " ID: " + truckEntityToEdit.getTruckId());

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in TruckServiceImpl - editTruck().", e);
            throw new LogiwebServiceException(e);
        }
    }

    /**
     * Find all trucks.
     *
     * @return list of trucks or empty list if nothing found.
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault).
     */
    @Override
    @Transactional
    public List<TruckDTO> findAllTrucks() throws LogiwebServiceException {
        try {
            return converter.convertListTruckEntitiesToDTO(truckDAO.findAll());

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
    public TruckDTO findTruckById(Integer truckId) throws LogiwebServiceException {
        try {
            Truck truck = truckDAO.findById(truckId);

            if (truck == null) {
                return null;
            } else {
                return converter.convertTruckEntityToDTO(truck);
            }
        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in TruckServiceImpl - findTruckById().", e);
            throw new LogiwebServiceException(e);
        }
    }

    @Override //
    @Transactional
    public void removeAssignedOrderAndDriversFromTruck(Integer truckId) throws LogiwebServiceException {
        try {
            Truck truck = truckDAO.findById(truckId);

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
