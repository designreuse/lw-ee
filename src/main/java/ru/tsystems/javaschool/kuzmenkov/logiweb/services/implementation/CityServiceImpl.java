package ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.CityDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.CityService;

import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
@Service("cityService")
public class CityServiceImpl implements CityService {

    @Autowired
    private CityDAO cityDAO;

    @Override
    @Transactional
    public List<City> findAllCities() throws LogiwebDAOException {
        return cityDAO.findAll();
    }

    @Override
    @Transactional
    public City findCityById(Integer cityId) throws LogiwebDAOException {
        return cityDAO.findById(cityId);
    }
}
