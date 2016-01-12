package ru.tsystems.javaschool.kuzmenkov.logiweb.service.implementation.junit4spring;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.OrderDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.OrderDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.OrderService;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Nikolay Kuzmenkov.
 */
@SuppressWarnings("deprecation")
@ContextConfiguration(locations = "/spring-config.xml")
public class OrderServiceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private OrderService orderService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void insertData() {
        JdbcTestUtils.executeSqlScript(jdbcTemplate, new FileSystemResource(createScript), false);
    }
    @After
    public void deleteData() {
        JdbcTestUtils.executeSqlScript(jdbcTemplate, new FileSystemResource(deleteScript), false);
    }

    private static final String createScript = "src/main/resources/sql/create-data-order.sql";
    private static final String deleteScript = "src/main/resources/sql/remove-data-order.sql";

    /**
     * Tests reading order.
     */
    @Test
    public void testFindAllOrders() {
        List<Order> result = orderService.findAllOrders();
        int size = orderService.findAllOrders().size();
        assertTrue(size > 0);
        assertNotNull(result);
    }

    @Test
    public void testFindOrderById() {
        OrderDTO result = orderService.findOrderById(900);
        assertTrue(result.getOrderId() == 900);
    }
}
