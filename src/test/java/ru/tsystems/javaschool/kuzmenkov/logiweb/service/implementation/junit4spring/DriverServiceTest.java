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
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.DriverDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.CityService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
        DriverDTO driver = new DriverDTO();
        driver.setFirstName("testFirstName2");
        driver.setLastName("testLastName2");
        driver.setPersonalNumber(22222);
        driver.setCurrentCityId(500);

        driverService.addNewDriver(driver);
        int b = driverService.findAllDrivers().size();

        assertTrue(b == a + 1);
        driverService.deleteDriver(driverService.getDriverByPersonalNumber(22222).getDriverId());
    }

    //a test to check the "read" method
    @Test
    public void testDriverRead() throws LogiwebServiceException {
        DriverDTO driverToRead = driverService.findDriverById(501);
        assertTrue(driverToRead.getPersonalNumber() == 11111);
    }

    //a test to check the "delete" method
    @Test
    public void testDriverDelete() throws LogiwebServiceException, LogiwebValidationException {
        DriverDTO driverToDelete = driverService.findDriverById(501);
        driverService.deleteDriver(driverToDelete.getDriverId());
        assertNull(driverService.findDriverById(501));
    }

    //a test to check the "getAll" method
    @Test
    public void testDriverFindAll() throws LogiwebServiceException {
        Set<DriverDTO> driverList = driverService.findAllDrivers();
        assertTrue(driverList.size() > 1);
    }

    //a test to check the "getDriverByPersonalNumber" method success
    @Test
    public void testDriverGetByPersonalNumberSuccess() throws LogiwebServiceException {
        Driver driver = driverService.getDriverByPersonalNumber(11111);
        assertNotNull(driver);
    }

    //a test to check the "getContractByNumber" method for return "null" result
    @Test
    public void testDriverGetByPersonalNumberWithNullResult() throws LogiwebServiceException {
        Driver driver = driverService.getDriverByPersonalNumber(1111122);
        assertNull(driver);
    }
}
