package ru.tsystems.javaschool.kuzmenkov.logiweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.CityService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.TruckService;

import java.util.List;
import java.util.Set;

/**
 * @author Nikolay Kuzmenkov.
 */
@Controller
public class TruckController {

    @Autowired
    private CityService cityService;
    @Autowired
    private TruckService truckService;

    @RequestMapping(value = {"truck"})
    public String showTrucks(Model model) throws LogiwebServiceException {
        List<Truck> trucks = truckService.findAllTrucks();
        model.addAttribute("trucks", trucks);
        model.addAttribute("cities", cityService.findAllCities());

        return "truck/TruckList";
    }

    @RequestMapping(value = {"truck/new"}, method = RequestMethod.GET)
    public String showFormForNewTruck(Model model) throws LogiwebServiceException {
        model.addAttribute("formAction", "new");
        model.addAttribute("truckFromForm", new Truck());
        model.addAttribute("cities", cityService.findAllCities());

        return "truck/AddOrEditTruck";
    }
}
