package ru.tsystems.javaschool.kuzmenkov.logiweb.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.CityService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nikolay Kuzmenkov.
 */
@Component
public class CitiesUtil {

    @Autowired
    private CityService cityService;

    public Model addAllCitiesToModel(Model model) {
        Map<Integer, City> cities = new HashMap<>();

        for (City city : cityService.findAllCities()) {
            cities.put(city.getCityId(), city);
        }

        model.addAttribute("cities", cities);

        return model;
    }
}
