package ru.tsystems.javaschool.kuzmenkov.logiweb.services;

import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.TruckDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;

import java.util.List;

/**
 * Business logic related to trucks.
 *
 * @author Nikolay Kuzmenkov.
 */
public interface TruckService {

    /**
     * Add new truck.
     *
     * @param newTruckDTO newTruckDTO
     * @return same truck.
     * @throws LogiwebDAOException if unexpected exception occurred on lower
     *              level (not user fault).
     * @throws LogiwebValidationException if truck don't have all required fields or not unique truck number.
     */
    Integer addNewTruck(TruckDTO newTruckDTO) throws LogiwebDAOException, LogiwebValidationException;

    /**
     * Edit truck.
     *
     * @param editedTruckDTO
     * @throws LogiwebValidationException
     *             if truck don't have all required fields or have not unique license
     *             plate
     * @throws LogiwebDAOException
     *             if unexpected exception occurred on lower level (not user
     *             fault)
     */
    void editTruck(TruckDTO editedTruckDTO) throws LogiwebDAOException, LogiwebValidationException;

    /**
     * Find all trucks.
     *
     * @return list of trucks or empty list if nothing found.
     * @throws LogiwebDAOException if unexpected exception occurred on lower level (not user fault).
     */
    List<TruckDTO> findAllTrucks() throws LogiwebDAOException;

    List<TruckDTO> findFreeAndUnbrokenByFreightCapacity(Float minFreightWeightCapacity) throws LogiwebDAOException;

    /**
     * Find truck by truck ID.
     *
     * @param truckId
     * @return truck by this truck ID or null.
     * @throws LogiwebDAOException if unexpected exception occurred on lower level (not user fault).
     */
    TruckDTO findTruckById(Integer truckId) throws LogiwebDAOException;

    /**
     * Remove assignment to order and drivers for this truck.
     *
     * @param truckId
     *
     */
    void removeAssignedOrderAndDriversFromTruck(Integer truckId) throws LogiwebDAOException, LogiwebValidationException;

    void removeTruck(Integer truckId) throws LogiwebDAOException, LogiwebValidationException;


}
