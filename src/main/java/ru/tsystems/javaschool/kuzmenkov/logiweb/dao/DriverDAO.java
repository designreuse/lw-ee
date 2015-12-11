package ru.tsystems.javaschool.kuzmenkov.logiweb.dao;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;

import java.util.List;

/**
 * Created by Nikolay on 13.11.2015.
 */
public interface DriverDAO extends AbstractDAO<Driver> {

    Driver findDriverByPersonalNumber(Integer driverPersonalNumber) throws LogiwebDAOException;

    List<Driver> findByCityWhereNotAssignedToTruck(City city) throws LogiwebDAOException;
}
