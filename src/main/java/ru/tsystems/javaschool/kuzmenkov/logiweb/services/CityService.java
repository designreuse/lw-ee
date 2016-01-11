package ru.tsystems.javaschool.kuzmenkov.logiweb.services;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;

import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
public interface CityService {

    List<City> findAllCities() throws LogiwebDAOException;

    City findCityById(Integer cityId) throws LogiwebDAOException;
}
