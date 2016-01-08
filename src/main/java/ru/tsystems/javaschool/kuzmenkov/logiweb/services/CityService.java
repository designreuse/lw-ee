package ru.tsystems.javaschool.kuzmenkov.logiweb.services;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;

import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
public interface CityService {

    List<City> findAllCities() throws LogiwebServiceException;

    City findCityById(Integer cityId) throws LogiwebServiceException;
}
