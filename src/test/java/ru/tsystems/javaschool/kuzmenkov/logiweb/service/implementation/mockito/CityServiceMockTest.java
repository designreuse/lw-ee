package ru.tsystems.javaschool.kuzmenkov.logiweb.service.implementation.mockito;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.CityDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation.CityServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author Nikolay Kuzmenkov.
 */
public class CityServiceMockTest {

    @InjectMocks
    private CityServiceImpl cityServiceImpl;
    @Mock
    private CityDAO cityDAOMock;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindCityByIdSuccess() {
        City testCity = new City();
        testCity.setCityId(1);

        when(cityDAOMock.findById(1)).thenReturn(testCity);
        assertNotNull(cityServiceImpl.findCityById(1));

    }

    @Test
    public void testFindAllCitiesSuccess() {
        List<City> allCities = new ArrayList<>();
        allCities.add(new City());

        when(cityDAOMock.findAll()).thenReturn(allCities);
        List<City> result = cityServiceImpl.findAllCities();

        assertNotNull(result);
        assertEquals(allCities, result);
    }
}
