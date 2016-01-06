package ru.tsystems.javaschool.kuzmenkov.logiweb.service.implementation.junit4spring;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.kuzmenkov.logiweb.controllers.model.ModelAttributeDriver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.CityService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;

import static org.junit.Assert.assertTrue;

/**
 * @author Nikolay Kuzmenkov.
 */
@SuppressWarnings("deprecation")
@ContextConfiguration(locations = "/spring-config.xml")
public class DriverServiceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private CityService cityService;
    @Autowired
    private DriverService driverService;
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

    private static final String createScript = "src/main/resources/sql/create-data-driver.sql";
    private static final String deleteScript = "src/main/resources/sql/remove-data-driver.sql";

    //a test to check the "create" method
    @Test
    @Transactional
    @Rollback(true)
    public void testDriverAddNewDriver() throws LogiwebServiceException, LogiwebValidationException {
        int a = driverService.findAllDrivers().size();
        ModelAttributeDriver driver = new ModelAttributeDriver();
        driver.setFirstName("testFirstName2");
        driver.setLastName("testLastName2");
        driver.setPersonalNumber(22222);
        driver.setCurrentCityFK(500);

        driverService.addNewDriver(driver);
        int b = driverService.findAllDrivers().size();

        assertTrue(b == a + 1);
        driverService.deleteDriver(driverService.getDriverByPersonalNumber(22222).getDriverId());
    }
}
