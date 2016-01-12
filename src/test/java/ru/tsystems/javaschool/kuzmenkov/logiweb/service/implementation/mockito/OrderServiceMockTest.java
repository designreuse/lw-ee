package ru.tsystems.javaschool.kuzmenkov.logiweb.service.implementation.mockito;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.OrderDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.TruckDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.OrderDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Freight;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.OrderStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.TruckStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation.OrderServiceImpl;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.EntityDTODataConverter;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Nikolay Kuzmenkov.
 */
public class OrderServiceMockTest {

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;
    @Mock
    private EntityDTODataConverter converter;
    @Mock
    private OrderDAO orderDAOMock;
    @Mock
    private TruckDAO truckDAOMock;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    // If method return "null"
    @Test
    public void testFindOrderByIdWhenReturnNull() {
        when(orderDAOMock.findById(1)).thenReturn(null);
        OrderDTO result = orderServiceImpl.findOrderById(1);
        assertNull(result);
    }


    @Test(expected = LogiwebValidationException.class)
    public void testAssignTruckToOrderWhenTruckIsNotOk() throws LogiwebValidationException {
        Truck testTruck = new Truck();
        testTruck.setTruckId(1);
        testTruck.setTruckStatus(TruckStatus.FAULTY);
        Order testOrder = new Order();
        testOrder.setOrderLines(new HashSet<>(Arrays.asList(new Freight())));
        testTruck.setOrderForThisTruck(testOrder);

        when(truckDAOMock.findById(1)).thenReturn(testTruck);
        when(orderDAOMock.findById(1)).thenReturn(testOrder);

        orderServiceImpl.assignTruck(1, 1);
    }

    @Test
    public void testAddNewOrder() {
        orderServiceImpl.addNewOrder();
        verify(orderDAOMock, times(1)).create(any(Order.class));
    }

    @Test
    public void testSetReadyStatusForOrderWhenEverythingOk() throws LogiwebValidationException {
        Order testOrder = createValidTestOrder();
        when(orderDAOMock.findById(testOrder.getOrderId())).thenReturn(testOrder);

        orderServiceImpl.setReadyStatusForOrder(testOrder.getOrderId());
        verify(orderDAOMock, times(1)).update(testOrder);
    }

    private Order createValidTestOrder() {
        Truck truck = new Truck();
        truck.setDriverCount(1);
        truck.setDriversInTruck(new HashSet<>(Arrays.asList(new Driver())));

        Order order = new Order();
        order.setOrderLines(new HashSet<>(Arrays.asList(new Freight())));
        order.setOrderStatus(OrderStatus.CREATED);
        order.setAssignedTruckFK(truck);

        return order;
    }
}
