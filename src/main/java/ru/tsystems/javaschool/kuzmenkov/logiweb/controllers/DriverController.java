package ru.tsystems.javaschool.kuzmenkov.logiweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.tsystems.javaschool.kuzmenkov.logiweb.controllers.model.ModelAttributeDriver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.Impl.TruckServiceImpl;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.TruckService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.CitiesUtil;

import javax.validation.Valid;
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

    @RequestMapping(value = "driver/new", method = RequestMethod.GET)
    public String showFormForAddNewDriver (Model model) throws LogiwebServiceException {
        model.addAttribute("formAction", "new");
        model.addAttribute("driverFromForm", new ModelAttributeDriver());
        citiesUtil.addAllCitiesToModel(model);

        return "driver/AddOrEditDriver";
    }

    @RequestMapping(value = {"driver/new"}, method = RequestMethod.POST)
    public String addNewDriver(
            @ModelAttribute("driverFromForm") @Valid ModelAttributeDriver driverFromForm,
            BindingResult result, Model model) throws LogiwebServiceException {

        if (result.hasErrors()) {

            model.addAttribute("driverModel", driverFromForm);
            citiesUtil.addAllCitiesToModel(model);
            model.addAttribute("formAction", "new");

            return "driver/AddOrEditDriver";
        }

        try {
            Integer newDriverId = driverService.addNewDriver(driverFromForm);

            return "redirect:/driver/" + newDriverId;

        } catch (LogiwebValidationException e) {
            model.addAttribute("error", e.getMessage());
            citiesUtil.addAllCitiesToModel(model);
            model.addAttribute("formAction", "new");
            return "driver/AddOrEditDriver";
        }
    }
}
