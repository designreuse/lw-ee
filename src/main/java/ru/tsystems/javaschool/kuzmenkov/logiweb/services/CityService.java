package ru.tsystems.javaschool.kuzmenkov.logiweb.services;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;

import java.util.List;

/**
 * Data manipulation and business logic related to Cities.
 *
 * @author Nikolay Kuzmenkov.
 */
public interface CityService {

    /**
     * Find all cities.
     *
     * @return empty set if none found
     * @throws LogiwebDAOException
     *             if something unexpected happened on lower level
     */
    List<City> findAllCities() throws LogiwebDAOException;

    /**
     * Find city by its ID.
     *
     * @param cityId
     * @return null if city not found
     * @throws LogiwebDAOException
     *             if something unexpected happened on lower level
     */
    City findCityById(Integer cityId) throws LogiwebDAOException;
}
