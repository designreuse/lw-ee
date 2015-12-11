package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import org.apache.log4j.Logger;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.DriverDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Nikolay on 13.11.2015.
 */
public class DriverDAOImpl extends AbstractDAOImpl<Driver> implements DriverDAO {

    private static final Logger LOGGER = Logger.getLogger(DriverDAOImpl.class);

    public DriverDAOImpl(Class<Driver> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);
    }

    @Override
    public Driver findDriverByPersonalNumber(Integer driverPersonalNumber) throws LogiwebDAOException{
        Driver queryResult = null;

        try {
            EntityManager entityManager = getEntityManager();

            Query query = entityManager.createQuery("SELECT dr FROM Driver dr " +
                    "WHERE dr.personalNumber = :driverPersonalNumber", Driver.class);
            query.setParameter("driverPersonalNumber", driverPersonalNumber);
            List<Driver> resultList = query.getResultList();

            if (!resultList.isEmpty()) {
                queryResult = resultList.get(0);
            }

        } catch (Exception e) {
            LOGGER.warn("Exception in DriverDAOImpl - findDriverByPersonalNumber().", e);
            throw new LogiwebDAOException(e);
        }

        return queryResult;
    }

    @Override
    public List<Driver> findByCityWhereNotAssignedToTruck(City city) throws LogiwebDAOException {
        List<Driver> queryResult;

        try {
            EntityManager em = getEntityManager();

            Query query = em.createQuery("SELECT dr FROM Driver dr WHERE dr.currentTruckFK IS NULL"
                    + " AND dr.currentCityFK = :city", Driver.class);
            query.setParameter("city", city);

            queryResult = query.getResultList();

        } catch (Exception e) {
            LOGGER.warn(e);
            throw new LogiwebDAOException(e);
        }

        return queryResult;
    }
}
