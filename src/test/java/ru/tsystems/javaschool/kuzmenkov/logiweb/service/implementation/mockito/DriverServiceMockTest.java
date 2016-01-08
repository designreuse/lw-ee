package ru.tsystems.javaschool.kuzmenkov.logiweb.service.implementation.mockito;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.*;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.DriverDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.UserService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation.DriverServiceImpl;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.EntityDTODataConverter;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * @author Nikolay Kuzmenkov.
 */
public class DriverServiceMockTest {

    @InjectMocks
    private DriverServiceImpl driverServiceImpl;

    @Mock
    private UserService userServiceMock;

    @Mock
    private EntityDTODataConverter converter;

    @Mock
    private DriverDAO driverDAOMock;
    @Mock
    private TruckDAO truckDAOMock;
    @Mock
    private OrderDAO orderDAOMock;
    @Mock
    private UserDAO userDAOMock;

    /**
     * Populate mocks.
     * @throws LogiwebDAOException
     */
    /*@Before
    public void setupMocks() throws LogiwebDAOException {
        cityDAOMock = mock(CityDAO.class);
        driverDAOMock = mock(DriverDAO.class);
        truckDAOMock = mock(TruckDAO.class);

        driverService = new DriverServiceImpl(driverDAOMock, truckDAOMock);
    }*/

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test: assignDriverToTruck
     * Case: truck not exist
     */
    @Test(expected = LogiwebValidationException.class)
    public void testAssignDriverToTruckWhenTruckNotExist() throws LogiwebDAOException, LogiwebServiceException, LogiwebValidationException {
        when(driverDAOMock.findById(1)).thenReturn(new Driver());
        when(truckDAOMock.findById(1)).thenReturn(null);

        driverServiceImpl.assignDriverToTruck(1, 1);
    }

    /**
     * Test: assignDriverToTruck
     * Case: driver not exist
     */
    @Test(expected = LogiwebValidationException.class)
    public void testAssignDriverToTruckWhenDriverNotExist() throws LogiwebDAOException, LogiwebServiceException, LogiwebValidationException {
        when(driverDAOMock.findById(1)).thenReturn(null);
        when(truckDAOMock.findById(1)).thenReturn(new Truck());

        driverServiceImpl.assignDriverToTruck(1, 1);
    }

    /**
     * Test: assignDriverToTruck
     * Case: check that truck is added to driver as well as
     * driver to truck
     * @throws LogiwebServiceException
     */
    @Test
    public void testAssignDriverToTruckThatTruckIsAddedToDriver() throws LogiwebDAOException, LogiwebServiceException, LogiwebValidationException {
        Driver d = new Driver();
        Truck t = new Truck();
        t.setDriverCount(1);

        when(driverDAOMock.findById(1)).thenReturn(d);
        when(truckDAOMock.findById(1)).thenReturn(t);

        driverServiceImpl.assignDriverToTruck(1, 1);

        Assert.assertTrue(d.getCurrentTruckFK() == t);
        Assert.assertTrue(t.getDriversInTruck().contains(d));

    }

    /**
     * Test: addDriverWithAccount
     * Case: driver with same employee id already exists
     */
    @Test(expected = LogiwebValidationException.class)
    public void testAddDriverWhenDriverWithSameEmpIdExists() throws LogiwebServiceException, LogiwebDAOException, LogiwebValidationException {
        DriverDTO driverModel = new DriverDTO();
        driverModel.setPersonalNumber(1);
        when(driverDAOMock.findDriverByPersonalNumber(1)).thenReturn(new Driver());
        driverServiceImpl.addNewDriver(driverModel);
    }

    /**
     * Test: addDriverWithAccount
     * Case: driver successfully added
     */
    /*
    @Test
    public void testAddDriverWhenDriverWhenEverythingOk() throws LogiwebServiceException, LogiwebDAOException, LogiwebValidationException {
        DriverDTO driverModel = new DriverDTO();
        driverModel.setPersonalNumber(1);

        driverServiceImpl.addNewDriver(driverModel);

        Mockito.verify(driverDAOMock, times(1)).create(any(Driver.class));
    }*/

    /**
     * Test: removeDriverAndAccount
     * Case: driver not exist
     */
    @Test(expected = LogiwebValidationException.class)
    public void testRemoveDriverAndAccountWhenDriverNotExist() throws LogiwebServiceException, LogiwebDAOException, LogiwebValidationException {
        Driver d = new Driver();
        d.setCurrentTruckFK(new Truck());
        when(driverDAOMock.findById(1)).thenReturn(null);

        driverServiceImpl.deleteDriver(1);
    }
}
