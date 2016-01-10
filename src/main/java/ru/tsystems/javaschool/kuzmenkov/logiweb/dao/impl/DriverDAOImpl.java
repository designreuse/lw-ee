package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.DriverDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;

import javax.persistence.Query;
import java.util.List;

/**
 * CRUD operations for Driver entity (JPA implementation).
 *
 * @author Nikolay Kuzmenkov.
 */
@Repository("driverDAO")
public class DriverDAOImpl extends AbstractDAOImpl<Driver> implements DriverDAO {

    /**
     */
    private static final Logger LOGGER = Logger.getLogger(DriverDAOImpl.class);

    @Override //
    public Driver findDriverByPersonalNumber(Integer driverPersonalNumber)
            throws LogiwebDAOException {
        try {
            Driver queryResult = null;
            Query query = getEntityManager().createQuery("SELECT dr FROM Driver dr "
                    + "WHERE dr.personalNumber = :driverPersonalNumber", Driver.class);
            query.setParameter("driverPersonalNumber", driverPersonalNumber);
            @SuppressWarnings("unchecked")
            List<Driver> resultList = query.getResultList();

            if (!resultList.isEmpty()) {
                queryResult = resultList.get(0);
            }

            return queryResult;

        } catch (Exception e) {
            LOGGER.warn("Exception in DriverDAOImpl - findDriverByPersonalNumber().", e);
            throw new LogiwebDAOException(e);
        }
    }

    @Override //
    public List<Driver> findByCityWhereNotAssignedToTruck(City city) throws LogiwebDAOException {
        try {
            Query query = getEntityManager().createQuery("SELECT dr FROM Driver dr WHERE dr.currentTruckFK IS NULL"
                    + " AND dr.currentCityFK = :city", Driver.class);
            query.setParameter("city", city);

            @SuppressWarnings("unchecked")
            List<Driver> queryResult = query.getResultList();

            return queryResult;

        } catch (Exception e) {
            LOGGER.warn(e);
            throw new LogiwebDAOException(e);
        }
    }
}
