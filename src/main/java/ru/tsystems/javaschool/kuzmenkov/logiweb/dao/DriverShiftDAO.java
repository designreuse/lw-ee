package ru.tsystems.javaschool.kuzmenkov.logiweb.dao;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.DriverShift;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;

import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
public interface DriverShiftDAO extends AbstractDAO<DriverShift> {

    /**
     * Find shift journal records for current month for drivers.
     *
     * Record is selected if shift started or ended in this month.
     * Shifts that are not ended yet are also selected.
     *
     * Method does not trim records. If record is started in previous month and ended
     * in this one, then it will be shown 'as is'.
     *
     * @param drivers
     * @return empty set if nothing was found or if empty set of drivers was past as param
     * @throws LogiwebDAOException if something unexpected happened
     */
    List<DriverShift> findThisMonthRecordsForDrivers(List<Driver> drivers) throws LogiwebDAOException;

    List<DriverShift> findThisMonthRecordsForDriver(Driver driver) throws LogiwebDAOException;
}
