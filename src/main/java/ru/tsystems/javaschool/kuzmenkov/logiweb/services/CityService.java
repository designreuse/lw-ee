package ru.tsystems.javaschool.kuzmenkov.logiweb.services;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;

import java.util.List;

/**
 * Created by Nikolay on 26.11.2015.
 */
public interface CityService {

    List<City> findAllCities() throws LogiwebServiceException; //

    City findCityById(Integer cityId) throws LogiwebServiceException;
}
