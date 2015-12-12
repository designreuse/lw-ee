package ru.tsystems.javaschool.kuzmenkov.logiweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.CitiesUtil;

import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
@Controller
public class DriverController {

    @Autowired
    DriverService driverService;
    @Autowired
    CitiesUtil citiesUtil;

    @RequestMapping("driver")
    public String showDrivers(Model model) throws LogiwebServiceException {
        List<Driver> drivers = driverService.findAllDrivers();
        model.addAttribute("drivers", drivers);

        for (Driver driver : drivers) {
            driver.setWorkingHoursThisMonth(driverService
                    .calculateWorkingHoursForDriver(driver.getDriverId()));
        }
        citiesUtil.addAllCitiesToModel(model);

        return "driver/DriverList";
    }
}
