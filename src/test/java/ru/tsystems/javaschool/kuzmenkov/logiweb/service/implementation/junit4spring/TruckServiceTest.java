package ru.tsystems.javaschool.kuzmenkov.logiweb.service.implementation.junit4spring;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.TruckDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.TruckStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.TruckService;

import static org.junit.Assert.assertTrue;

/**
 * @author Nikolay Kuzmenkov.
 */
@ContextConfiguration(locations = "/spring-config.xml")
public class TruckServiceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private TruckService truckService;

    /**
     * Tests creating truck.
     */
    @Test
    public void testCreateTruck() throws LogiwebValidationException {

        int sizeAllTrucksBeforeTest = truckService.findAllTrucks().size();

        TruckDTO testTruck = new TruckDTO();
        testTruck.setTruckNumber("XX44444");
        testTruck.setDriverCount(1);
        testTruck.setCapacity(2f);
        testTruck.setTruckStatus(TruckStatus.WORKING);
        testTruck.setCurrentCityId(1);
        Integer idOfTestTruck = truckService.addNewTruck(testTruck);
        int sizeAllTrucksAfterTest = truckService.findAllTrucks().size();

        assertTrue(sizeAllTrucksAfterTest > sizeAllTrucksBeforeTest);

        truckService.removeTruck(idOfTestTruck);
    }
}
