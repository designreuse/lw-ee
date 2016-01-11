package ru.tsystems.javaschool.kuzmenkov.logiweb;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.*;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertNotNull;

/**
 * A test of entity manager injection.
 */
@ContextConfiguration(locations = "/spring-config.xml")
public class DependencyInjectionTest extends AbstractJUnit4SpringContextTests {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private DriverService driverService;
    @Autowired
    private TruckService truckService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private FreightService freightService;
    @Autowired
    private UserService userService;
    @Autowired
    private CityService cityService;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private DriverDAO driverDAO;
    @Autowired
    private DriverShiftDAO driverShiftDAO;
    @Autowired
    private CityDAO cityDAO;
    @Autowired
    private FreightDAO freightDAO;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private TruckDAO truckDAO;

    @Test
    public void testEntityManager() {
        assertNotNull(entityManager);
    }
    @Test
    public void testUserService() {

        assertNotNull(userService);
    }
    @Test
    public void testDriverService() {
        assertNotNull(driverService);

    }
    @Test
    public void testTruckService() {
        assertNotNull(truckService);

    }
    @Test
    public void testOrderService() {
        assertNotNull(orderService);

    }
    @Test
    public void testFreightService() {
        assertNotNull(freightService);
    }
    @Test
    public void testCityService() {
        assertNotNull(cityService);
    }

    @Test
    public void testUserDAO() {
        assertNotNull(userDAO);
    }
    @Test
    public void testDriverDAO() {
        assertNotNull(driverDAO);
    }
    @Test
    public void testDriverShiftDAO() {
        assertNotNull(driverShiftDAO);
    }
    @Test
    public void testTruckDAO() {
        assertNotNull(truckDAO);
    }
    @Test
    public void testOrderDAO() {
        assertNotNull(orderDAO);
    }
    @Test
    public void testFreightDAO() {
        assertNotNull(freightDAO);
    }
    @Test
    public void testCityDAO() {
        assertNotNull(cityDAO);
    }
}
