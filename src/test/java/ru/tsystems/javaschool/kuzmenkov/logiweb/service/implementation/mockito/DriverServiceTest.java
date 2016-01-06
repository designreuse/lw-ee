package ru.tsystems.javaschool.kuzmenkov.logiweb.service.implementation.mockito;

import org.junit.Before;
import org.junit.Test;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.CityDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.DriverDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.TruckDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation.DriverServiceImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Nikolay Kuzmenkov.
 */
public class DriverServiceTest {

    private DriverService driverService;

    private CityDAO cityDAOMock;
    private DriverDAO driverDAOMock;
    private TruckDAO truckDAOMock;

    /**
     * Populate mocks.
     * @throws LogiwebDAOException
     */
    @Before
    public void setupMocks() throws LogiwebDAOException {
        cityDAOMock = mock(CityDAO.class);
        driverDAOMock = mock(DriverDAO.class);
        truckDAOMock = mock(TruckDAO.class);

        driverService = new DriverServiceImpl(driverDAOMock, truckDAOMock);
    }

    /**
     * Test: assignDriverToTruck
     * Case: truck not exist
     */
    @Test(expected = LogiwebValidationException.class)
    public void testAssignDriverToTruckWhenTruckNotExist() throws LogiwebDAOException, LogiwebServiceException, LogiwebValidationException {
        setupMocks();
        when(driverDAOMock.findById(1)).thenReturn(new Driver());
        when(truckDAOMock.findById(1)).thenReturn(null);

        driverService.assignDriverToTruck(1, 1);
    }
}
