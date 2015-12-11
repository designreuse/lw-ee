package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.DriverShiftDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.DriverShift;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.DateUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
public class DriverShiftDAOImpl extends AbstractDAOImpl<DriverShift> implements DriverShiftDAO {


    public DriverShiftDAOImpl(Class<DriverShift> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);
    }

    @Override
    public List<DriverShift> findThisMonthRecordsForDrivers(List<Driver> drivers) throws LogiwebDAOException {
        List<DriverShift> queryResult;

        if(drivers == null || drivers.isEmpty()) {
            return new ArrayList<DriverShift>(0);
        }

        try {
            EntityManager em = getEntityManager();

            Date firstDateOfCurrentMonth = DateUtil.getFirstDateOfCurrentMonth();
            Date firstDateOfNextMonth = DateUtil.getFirstDayOfNextMonth();

            String queryString = "SELECT ds FROM DriverShift ds WHERE ds.driverForThisShiftFK IN :drivers"
                    + " AND ((driverShiftEnd BETWEEN :firstDayOfMonth AND :firstDayOfNextMonth)"
                    + " OR (ds.driverShiftBegin BETWEEN :firstDayOfMonth AND :firstDayOfNextMonth))";

            Query query = em.createQuery(queryString, DriverShift.class)
                    .setHint("org.hibernate.cacheable", false);     //fix for strange behavior of hiber
            query.setParameter("drivers", drivers);
            query.setParameter("firstDayOfMonth", firstDateOfCurrentMonth);
            query.setParameter("firstDayOfNextMonth", firstDateOfNextMonth);


            queryResult = query.getResultList();


        } catch (Exception e) {
            //LOG.warn(e);
            throw new LogiwebDAOException(e);
        }

        return new ArrayList<DriverShift>(queryResult);
    }

    @Override
    public List<DriverShift> findThisMonthRecordsForDriver(Driver driver) throws LogiwebDAOException {
        if (driver == null) {
            return new ArrayList<DriverShift>();
        }

        List<Driver> drivers = new ArrayList<Driver>();
        drivers.add(driver);

        return findThisMonthRecordsForDrivers(drivers);
    }
}
