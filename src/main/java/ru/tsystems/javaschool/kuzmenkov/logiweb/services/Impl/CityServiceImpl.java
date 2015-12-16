package ru.tsystems.javaschool.kuzmenkov.logiweb.services.Impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.CityDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.CityService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
@Service("cityService")
public class CityServiceImpl implements CityService {

    private static final Logger LOGGGER = Logger.getLogger(TruckServiceImpl.class);
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CityDAO cityDAO;

    @Override
    @Transactional
    public List<City> findAllCities() throws LogiwebServiceException { //
        try {
            return cityDAO.findAll();

        } catch (LogiwebDAOException e) {
            //LOG.warn("Something unexcpected happend.");
            throw new LogiwebServiceException(e);
        }
    }

    @Override
    public City findCityById(Integer cityId) throws LogiwebServiceException {
        City cityResult;

        try {
            entityManager.getTransaction().begin();
            cityResult = cityDAO.findById(cityId);
            entityManager.getTransaction().commit();

        } catch (LogiwebDAOException e) {
            LOGGGER.warn("Exception in CityServiceImpl - findCityById.", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }

        return cityResult;
    }
}
