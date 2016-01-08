package ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.CityDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.CityService;

import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
@Service("cityService")
public class CityServiceImpl implements CityService {

    private static final Logger LOGGER = Logger.getLogger(CityServiceImpl.class);
    @Autowired
    private CityDAO cityDAO;

    @Override
    @Transactional
    public List<City> findAllCities() throws LogiwebServiceException {
        try {
            return cityDAO.findAll();

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in CityServiceImpl - findAllCities().", e);
            throw new LogiwebServiceException(e);
        }
    }

    @Override
    @Transactional
    public City findCityById(Integer cityId) throws LogiwebServiceException {
        try {

            return cityDAO.findById(cityId);

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in CityServiceImpl - findCityById.", e);
            throw new LogiwebServiceException(e);
        }
    }
}
