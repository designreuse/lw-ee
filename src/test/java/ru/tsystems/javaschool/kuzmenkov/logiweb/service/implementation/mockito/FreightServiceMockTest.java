package ru.tsystems.javaschool.kuzmenkov.logiweb.service.implementation.mockito;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.FreightDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.OrderDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Freight;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.OrderStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.UserService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation.DriverServiceImpl;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation.FreightServiceImpl;

import static org.mockito.Mockito.when;

/**
 * @author Nikolay Kuzmenkov.
 */
public class FreightServiceMockTest {

    @InjectMocks
    private FreightServiceImpl freightServiceImpl;

    @Mock
    private FreightDAO freightDAOMock;
    @Mock
    private OrderDAO orderDAOMock;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    private Freight createValidTestFreight() {
        Freight testFreight = new Freight();

        City city1 = new City();
        city1.setCityId(1);
        City city2 = new City();
        city2.setCityId(2);
        Order order = new Order();
        order.setOrderId(1);

        testFreight.setDescription("testFreight");
        testFreight.setWeight(1f);
        testFreight.setCityFromFK(city1);
        testFreight.setCityFromFK(city2);
        testFreight.setOrderForThisFreightFK(order);

        return testFreight;
    }

    @Test(expected = LogiwebValidationException.class)
    public void testAddCargoWhenOrderHaveAssignedTruck() throws
            LogiwebServiceException, LogiwebDAOException, LogiwebValidationException {
        Freight testNewFreight = createValidTestFreight();
        //setMocksToPassInnerValidationInAddCargo(newCargo);

        Order wrongStatusOrder = new Order();
        wrongStatusOrder.setOrderStatus(OrderStatus.CREATED);
        wrongStatusOrder.setAssignedTruckFK(new Truck());

        when(orderDAOMock.findById(testNewFreight.getOrderForThisFreightFK().getOrderId()))
                .thenReturn(wrongStatusOrder);

        freightServiceImpl.addNewFreight(testNewFreight);
    }
}
