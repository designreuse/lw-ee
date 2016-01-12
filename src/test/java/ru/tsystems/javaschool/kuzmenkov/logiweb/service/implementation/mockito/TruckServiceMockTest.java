package ru.tsystems.javaschool.kuzmenkov.logiweb.service.implementation.mockito;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.TruckDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.TruckDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation.TruckServiceImpl;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.EntityDTODataConverter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
    private EntityDTODataConverter converter;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllTrucksSuccess() {
        List<Truck> allTrucks = new ArrayList<>();
        allTrucks.add(new Truck());
        List<TruckDTO> allDTOTrucks = new ArrayList<>();
        allDTOTrucks.add(new TruckDTO());

        when(truckDAOMock.findAll()).thenReturn(allTrucks);
        when(converter.convertListTruckEntitiesToDTO(allTrucks)).thenReturn(allDTOTrucks);
        List<TruckDTO> result = truckServiceImpl.findAllTrucks();

        assertNotNull(result);
        assertEquals(allDTOTrucks, result);
    }

    /**
     * Test: editTruck
    * Case: everything Ok
    */
    @Test
    public void testEditTruckWhenEverythingOk() throws LogiwebValidationException {
        TruckDTO testTruck = new TruckDTO();
        testTruck.setTruckId(1);
        testTruck.setTruckNumber("testNumber");
        testTruck.setDriverCount(1);
        testTruck.setCapacity(1f);
        testTruck.setCurrentCityId(1);

        when(truckDAOMock.findById(1)).thenReturn(new Truck());

        truckServiceImpl.editTruck(testTruck);

        Mockito.verify(truckDAOMock, times(1)).update(any(Truck.class));
    }

    /**
     * Test: editTruck
     * Case: license plate is occupied by another truck
     */
    @Test(expected = LogiwebValidationException.class)
    public void testEditTruckWhenTruckNumberOccupied() throws LogiwebValidationException {
        //needed to not fail other checks and validations
        TruckDTO testTruck = new TruckDTO();
        testTruck.setTruckId(1);
        testTruck.setTruckNumber("same");

        Truck sameTestTruck = new Truck();
        sameTestTruck.setTruckId(2); //not same id
        sameTestTruck.setTruckNumber("same");

        when(truckDAOMock.findTruckByTruckNumber("same")).thenReturn(sameTestTruck);
        when(truckDAOMock.findById(1)).thenReturn(new Truck());

        truckServiceImpl.editTruck(testTruck);
    }
}
