package ru.tsystems.javaschool.kuzmenkov.logiweb.dao;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;

import java.util.List;

/**
 * Interface for basic operations for Driver entity.
 *
 * @author Nikolay Kuzmenkov.
 */
public interface DriverDAO extends AbstractDAO<Driver> {

    Driver findDriverByPersonalNumber(Integer driverPersonalNumber) throws LogiwebDAOException;

    List<Driver> findByCityWhereNotAssignedToTruck(City city) throws LogiwebDAOException;
}
