package ru.tsystems.javaschool.kuzmenkov.logiweb.service.implementation.mockito;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.OrderDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.TruckDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Freight;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.TruckStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation.OrderServiceImpl;

import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.when;

/**
 * @author Nikolay Kuzmenkov.
 */
public class OrderServiceMockTest {

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;
    @Mock
    private OrderDAO orderDAOMock;
    @Mock
    private TruckDAO truckDAOMock;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = LogiwebValidationException.class)
    public void testAssignTruckToOrderWhenTruckIsNotOk() throws LogiwebValidationException {
        Truck testTruck = new Truck();
        testTruck.setTruckId(1);
        testTruck.setTruckStatus(TruckStatus.FAULTY);
        Order testOrder = new Order();
        testOrder.setOrderLines(new HashSet<>(Arrays.asList(new Freight())));

        when(truckDAOMock.findById(1)).thenReturn(testTruck);
        when(orderDAOMock.findById(1)).thenReturn(testOrder);

        orderServiceImpl.assignTruck(1, 1);
    }
}
