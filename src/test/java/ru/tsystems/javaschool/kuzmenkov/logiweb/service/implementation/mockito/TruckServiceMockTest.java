package ru.tsystems.javaschool.kuzmenkov.logiweb.service.implementation.mockito;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.TruckDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.TruckDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation.DriverServiceImpl;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation.TruckServiceImpl;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebValidator;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Nikolay Kuzmenkov.
 */
public class TruckServiceMockTest {

    @InjectMocks
    private TruckServiceImpl truckServiceImpl;
    @Mock
    private TruckDAO truckDAOMock;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
        //when(truckServiceImpl.validateTruckNumber("jj")).thenReturn(true);
    }

    /**
     * Test: addTruck
     * Case: when truck have invalid plate number
     */
    @Test(expected = LogiwebValidationException.class)
    public void testAddTruckWhenPlateValidationFailed() throws LogiwebValidationException, LogiwebServiceException, LogiwebDAOException {
        TruckDTO testTruck = new TruckDTO();
        testTruck.setTruckNumber("test");
        testTruck.setDriverCount(1);
        testTruck.setCapacity(1f);
        testTruck.setCurrentCityId(1);

        //when(truckServiceImpl.validateTruckNumber(any(String.class))).thenReturn(false);
        truckServiceImpl.addNewTruck(testTruck);
    }
}
