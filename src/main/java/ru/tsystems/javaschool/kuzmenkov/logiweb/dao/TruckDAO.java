package ru.tsystems.javaschool.kuzmenkov.logiweb.dao;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;

import java.util.List;

/**
 * Operations for Truck entity.
 *
 * @author Nikolay Kuzmenkov.
 */
public interface TruckDAO extends AbstractDAO<Truck> {

    List<Truck> findByMinCapacityWhereStatusOkAndNotAssignedToOrder(Float minCargoCapacity)  throws LogiwebDAOException;

    Truck findTruckByTruckNumber(String truckNumber) throws LogiwebDAOException;
}
