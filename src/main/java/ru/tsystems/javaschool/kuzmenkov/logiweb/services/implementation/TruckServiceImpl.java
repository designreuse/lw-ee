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
    private LogiwebValidator validator;
    @Autowired
    private TruckDAO truckDAO;

    /**
     * Add new truck.
     *
     * @param newTruckDTO
     * @return same truck.
     * @throws LogiwebDAOException if unexpected exception occurred on lower level (not user fault).
     * @throws LogiwebValidationException if truck don't have all required fields or not unique truck number.
     */
    @Override
    @Transactional
    public Integer addNewTruck(TruckDTO newTruckDTO) throws LogiwebDAOException, LogiwebValidationException {
        Truck truckWithSameTruckNumber = truckDAO.findTruckByTruckNumber(newTruckDTO.getTruckNumber());

        if (truckWithSameTruckNumber != null) {
            throw new LogiwebValidationException("Truck with number #" + newTruckDTO.getTruckNumber() +
                    " is already exist.");
        }

        Truck newTruckEntity = converter.convertTruckDTOToEntity(newTruckDTO);
        newTruckEntity.setTruckStatus(TruckStatus.WORKING);
        truckDAO.create(newTruckEntity);

        LOGGER.info("Truck created: truck number #" + newTruckEntity.getTruckNumber() + " ID: " + newTruckEntity.getTruckId());

        return newTruckEntity.getTruckId();
    }

    @Override
    @Transactional
    public void editTruck(TruckDTO editedTruckDTO) throws LogiwebDAOException, LogiwebValidationException {
        Truck truckWithSameNumber = truckDAO.findTruckByTruckNumber(editedTruckDTO.getTruckNumber());

        if (truckWithSameNumber != null && !(truckWithSameNumber.getTruckId().equals(editedTruckDTO.getTruckId()))) {
            throw new LogiwebValidationException("Truck number " + editedTruckDTO.getTruckNumber() + " is already in use.");
        }

        Truck truckEntityToEdit = truckDAO.findById(editedTruckDTO.getTruckId());
        populateAllowedTruckFieldsFromDTO(truckEntityToEdit, editedTruckDTO);

        truckDAO.update(truckEntityToEdit);
        LOGGER.info("Truck edited. Number " + truckEntityToEdit.getTruckNumber() + " ID: " + truckEntityToEdit.getTruckId());
    }

    /**
     * Find all trucks.
     *
     * @return list of trucks or empty list if nothing found.
     * @throws LogiwebDAOException if unexpected exception occurred on lower level (not user fault).
     */
    @Override
    @Transactional
    public List<TruckDTO> findAllTrucks() throws LogiwebDAOException {
        return converter.convertListTruckEntitiesToDTO(truckDAO.findAll());
    }

    @Override
    @Transactional
    public List<TruckDTO> findFreeAndUnbrokenByFreightCapacity(Float minFreightWeightCapacity) throws LogiwebDAOException {
        return converter.convertListTruckEntitiesToDTO(
                truckDAO.findByMinCapacityWhereStatusOkAndNotAssignedToOrder(minFreightWeightCapacity));
    }

    /**
     * Find truck by truck ID.
     *
     * @param truckId
     * @return truck by this truck ID or null.
     * @throws LogiwebDAOException if unexpected exception occurred on lower level (not user fault).
     */
    @Override
    @Transactional
    public TruckDTO findTruckById(Integer truckId) throws LogiwebDAOException {
        Truck truck = truckDAO.findById(truckId);

        if (truck == null) {
            return null;
        } else {
            return converter.convertTruckEntityToDTO(truck);
        }
    }

    @Override
    @Transactional
    public void removeAssignedOrderAndDriversFromTruck(Integer truckId) throws LogiwebDAOException, LogiwebValidationException {
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
    }

    @Override
    @Transactional
    public void removeTruck(Integer truckId) throws LogiwebDAOException, LogiwebValidationException {
        Truck truckToRemove = truckDAO.findById(truckId);
        truckDAO.delete(truckToRemove);
        LOGGER.info("Truck removed. Plate " + truckToRemove.getTruckNumber() + " ID: " + truckToRemove.getTruckId());
    }

    private Truck populateAllowedTruckFieldsFromDTO(Truck entityToEdit, TruckDTO source) {
        City city = new City();
        city.setCityId(source.getCurrentCityId());
        entityToEdit.setCurrentCityFK(city);
        entityToEdit.setCapacity(source.getCapacity());
        entityToEdit.setDriverCount(source.getDriverCount());
        entityToEdit.setTruckStatus(source.getTruckStatus());
        entityToEdit.setTruckNumber(source.getTruckNumber());

        return entityToEdit;
    }
}
