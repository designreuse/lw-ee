package ru.tsystems.javaschool.kuzmenkov.logiweb.service.implementation.mockito;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.TruckDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.TruckDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.OrderStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation.TruckServiceImpl;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebValidator;

import java.util.HashSet;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * @author Nikolay Kuzmenkov.
 */
public class TruckServiceMockTest {

    @InjectMocks
    private TruckServiceImpl truckServiceImpl;
    @Mock
    private TruckDAO truckDAOMock;
    @Mock
    private LogiwebValidator validatorMock;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
        when(validatorMock.validateTruckNumber(any(String.class))).thenReturn(true);
    }

    /**
     * Test: addTruck
     * Case: when truck have invalid plate number
     */
    @Test(expected = LogiwebValidationException.class)
    public void testAddTruckWhenPlateValidationFailed() throws LogiwebValidationException {
        TruckDTO testTruck = new TruckDTO();
        testTruck.setTruckNumber("test");
        testTruck.setDriverCount(1);
        testTruck.setCapacity(1f);
        testTruck.setCurrentCityId(1);

        when(validatorMock.validateTruckNumber(any(String.class))).thenReturn(false);
        truckServiceImpl.addNewTruck(testTruck);
    }

    /**
     * Test: editTruck
     * Case: id not set in model
     */
    @Test(expected = LogiwebValidationException.class)
    public void testEditTruckWhenIdNotSet() throws LogiwebValidationException {
        TruckDTO testTruck = new TruckDTO();
        testTruck.setTruckId(null);

        truckServiceImpl.editTruck(testTruck);
    }

    /**
     * Test: removeTruck
     * Case: truck have assigned order
     */
    @Test(expected = LogiwebValidationException.class)
    public void testRemoveTruckWhenTruckHaveAssignedOrder() throws LogiwebValidationException {
        Truck testTruck = new Truck();
        testTruck.setOrderForThisTruck(new Order());

        when(truckDAOMock.findById(1)).thenReturn(testTruck);

        truckServiceImpl.removeTruck(1);
    }

    /**
     * Test: removeAssignedOrderAndDriversFromTruck
     * Case: everything Ok
     */
    @Test
    public void testRemoveAssignedOrderWhenEverythingOk() throws LogiwebValidationException {
        Truck testTruck = new Truck();
        testTruck.setOrderForThisTruck(new Order());
        testTruck.setDriversInTruck(new HashSet<>());

        when(truckDAOMock.findById(1)).thenReturn(testTruck);

        truckServiceImpl.removeAssignedOrderAndDriversFromTruck(1);
        Mockito.verify(truckDAOMock, times(1)).update(testTruck);
    }

    /**
     * Test: removeAssignedOrderAndDriversFromTruck
     * Case: truck not exist
     */
    @Test(expected = LogiwebValidationException.class)
    public void testRemoveAssignedOrderWhenTruckNotExist() throws LogiwebValidationException {
        when(truckDAOMock.findById(1)).thenReturn(null);

        truckServiceImpl.removeAssignedOrderAndDriversFromTruck(1);
    }

    /**
     * Test: removeAssignedOrderAndDriversFromTruck
     * Case: there is no order assigned to truck
     */
    @Test(expected = LogiwebValidationException.class)
    public void testRemoveAssignedOrderAndDriversFromTruckWhenNoOrder() throws LogiwebValidationException {
        Truck testTruck = new Truck();
        testTruck.setOrderForThisTruck(null);

        when(truckDAOMock.findById(1)).thenReturn(testTruck);

        truckServiceImpl.removeAssignedOrderAndDriversFromTruck(1);
    }

    /**
     * Test: removeAssignedOrderAndDriversFromTruck
     * Case: order have READY_TO_GO status - removal is forbidden
     */
    @Test(expected = LogiwebValidationException.class)
    public void testRemoveAssignedOrderWhenOrderStatusWrong() throws LogiwebValidationException {
        Truck testTruck = new Truck();
        Order testOrder = new Order();
        testOrder.setOrderStatus(OrderStatus.READY_TO_GO);
        testTruck.setOrderForThisTruck(testOrder);

        when(truckDAOMock.findById(1)).thenReturn(testTruck);

        truckServiceImpl.removeAssignedOrderAndDriversFromTruck(1);
    }
}
