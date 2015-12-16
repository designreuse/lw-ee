package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import org.springframework.stereotype.Repository;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.DriverShiftDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.DriverShift;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.DateUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CRUD operations for DriverShift entity (JPA implementation).
 *
 * @author Nikolay Kuzmenkov.
 */
@Repository("driverShiftDAO")
public class DriverShiftDAOImpl extends AbstractDAOImpl<DriverShift> implements DriverShiftDAO {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<DriverShift> findThisMonthRecordsForDrivers(List<Driver> drivers) throws LogiwebDAOException { //
        if(drivers == null || drivers.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            Date firstDateOfCurrentMonth = DateUtil.getFirstDateOfCurrentMonth();
            Date firstDateOfNextMonth = DateUtil.getFirstDayOfNextMonth();

            String queryString = "SELECT ds FROM DriverShift ds WHERE ds.driverForThisShiftFK IN :drivers"
                    + " AND ((driverShiftEnd BETWEEN :firstDayOfMonth AND :firstDayOfNextMonth)"
                    + " OR (ds.driverShiftBegin BETWEEN :firstDayOfMonth AND :firstDayOfNextMonth))";

            Query query = entityManager.createQuery(queryString, DriverShift.class)
                    .setHint("org.hibernate.cacheable", false);     //fix for strange behavior of hiber
            query.setParameter("drivers", drivers);
            query.setParameter("firstDayOfMonth", firstDateOfCurrentMonth);
            query.setParameter("firstDayOfNextMonth", firstDateOfNextMonth);

            @SuppressWarnings("unchecked")
            List<DriverShift> queryResult = query.getResultList();

            return queryResult;

        } catch (Exception e) {
            //LOG.warn(e);
            throw new LogiwebDAOException(e);
        }
    }

    @Override
    public List<DriverShift> findThisMonthRecordsForDrivers(Driver driver) throws LogiwebDAOException { //
        if (driver == null) {
            return new ArrayList<>();
        }

        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);

        return findThisMonthRecordsForDrivers(drivers);
    }
}
