package ru.tsystems.javaschool.kuzmenkov.logiweb.services;

import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.TruckDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
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
     * @throws LogiwebServiceException if unexpected exception occurred on lower
     *              level (not user fault).
     * @throws LogiwebValidationException if truck don't have all required fields or not unique truck number.
     */
    Integer addNewTruck(TruckDTO newTruckDTO) throws LogiwebServiceException;

    /**
     * Find all trucks.
     *
     * @return list of trucks or empty list if nothing found.
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault).
     */
    List<Truck> findAllTrucks() throws LogiwebServiceException;

    List<TruckDTO> findFreeAndUnbrokenByFreightCapacity(Float minFreightWeightCapacity) throws LogiwebServiceException;

    /**
     * Find truck by truck ID.
     *
     * @param truckId
     * @return truck by this truck ID or null.
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault).
     */
    Truck findTruckById(Integer truckId) throws LogiwebServiceException;

    void removeAssignedOrderAndDriversFromTruck(Integer truckId) throws LogiwebServiceException;

    void removeTruck(Integer truckId) throws LogiwebServiceException;


}
