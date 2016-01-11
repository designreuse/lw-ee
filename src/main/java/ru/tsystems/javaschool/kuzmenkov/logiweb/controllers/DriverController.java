package ru.tsystems.javaschool.kuzmenkov.logiweb.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.DriverDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.DriverStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.RecordNotFoundException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.CitiesUtil;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Nikolay Kuzmenkov.
 */
@Controller
public class DriverController {

    private static final Logger LOGGER = Logger.getLogger(DriverController.class);

    /**
     */
    @Autowired
    private DriverService driverService;
    /**
     */
    @Autowired
    private CitiesUtil citiesUtil;

    /**
     * @param model model
     * @return DriverList.jsp
     */
    @RequestMapping("driver")
    public String showDrivers(Model model) {
        Set<DriverDTO> drivers = driverService.findAllDrivers();
        model.addAttribute("drivers", drivers);

        for (DriverDTO driver : drivers) {
            driver.setWorkingHoursThisMonth(driverService
                    .calculateWorkingHoursForDriver(driver.getDriverId()));
        }
        citiesUtil.addAllCitiesToModel(model);

        return "driver/DriverList";
    }

    @RequestMapping(value = {"driver/new"}, method = RequestMethod.GET)
    public String showFormForAddNewDriver(Model model) {
        model.addAttribute("formAction", "new");
        model.addAttribute("driverFromForm", new DriverDTO());
        citiesUtil.addAllCitiesToModel(model);

        return "driver/AddOrEditDriver";
    }

    @RequestMapping(value = {"driver/{driverId}/edit"}, method = RequestMethod.GET)
    public String showFormForEditDriver(@PathVariable("driverId") Integer driverId, Model model) {
        model.addAttribute("formAction", "edit");
        DriverDTO driverToEdit = driverService.findDriverById(driverId);

        if (driverToEdit == null) {
            throw new RecordNotFoundException();
        }

        model.addAttribute("driverFromForm", driverToEdit);
        citiesUtil.addAllCitiesToModel(model);
        model.addAttribute("driverStatuses", DriverStatus.values());

        return "driver/AddOrEditDriver";

    }

    @RequestMapping(value = {"driver/new"}, method = RequestMethod.POST)
    public String addNewDriver(@ModelAttribute("driverFromForm") @Valid DriverDTO driverFromForm,
                               BindingResult result, Model model) throws NoSuchAlgorithmException {
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
            LOGGER.warn("Validation exception in method - addNewDriver(..)", e);
            model.addAttribute("error", e.getMessage());
            citiesUtil.addAllCitiesToModel(model);
            model.addAttribute("formAction", "new");
            return "driver/AddOrEditDriver";
        }
    }

    @RequestMapping(value = {"driver/{driverId}/edit"}, method = RequestMethod.POST)
    public String editDriver(@ModelAttribute("driverFromForm") @Valid DriverDTO editDriverFromForm,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("driverFromForm", editDriverFromForm);
            citiesUtil.addAllCitiesToModel(model);
            model.addAttribute("formAction", "edit");

            return "driver/AddOrEditDriver";
        }

        try {
            driverService.editDriver(editDriverFromForm);//convertDriverEmpIdToAccountNameByTemplate(editDriverFromForm.getPersonalNumber()));

            return "redirect:/driver/" + editDriverFromForm.getDriverId();

        } catch (LogiwebValidationException e) {
            model.addAttribute("error", e.getMessage());
            citiesUtil.addAllCitiesToModel(model);
            model.addAttribute("formAction", "edit");

            return "driver/AddOrEditDriver";
        }
    }

    /**
     * Removes driver by its ID received in 'driverID' parameter.
     */
    @RequestMapping(value = "driver/{driverId}/delete", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String deleteDriver(@PathVariable("driverId") Integer driverId, HttpServletResponse response) {
        try {
            driverService.deleteDriver(driverId);

            return "Driver deleted";

        } catch (LogiwebValidationException e) {
            LOGGER.warn("Validation exception in method - deleteDriver(..)", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/driver/{driverId}")
    public String showSingleDriver(@PathVariable("driverId") Integer driverId, Model model) {
        //authorizeAccesToDriverInfo(driverId);
        DriverDTO driverToShow = driverService.getDriverWithFullInfo(driverId);

        if (driverToShow == null) {
            throw new RecordNotFoundException();
        }

        if (driverToShow.getCoDriversIds() != null) {
            Map<Integer, DriverDTO> coDrivers = new HashMap<>();

            for (Integer coDriverId : driverToShow.getCoDriversIds()) {
                coDrivers.put(coDriverId, driverService.findDriverById(coDriverId));
            }

            model.addAttribute("coDrivers", coDrivers);
        }

        model.addAttribute("driver", driverToShow);
        citiesUtil.addAllCitiesToModel(model);

        return "driver/SingleDriver";
    }

    @RequestMapping(value = "order/{orderId}/edit/addDriverToTruck", method = RequestMethod.POST)
    @ResponseBody
    public String addDriverToTruck(@RequestParam("driversIds") Integer[] driversIds, @RequestParam("truckId") Integer truckId,
                                   HttpServletResponse response) {
        try {
            for (Integer driverId : driversIds) {
                driverService.assignDriverToTruck(driverId, truckId);
            }

            return "Drivers are added to truck";

        } catch (LogiwebValidationException e) {
            LOGGER.warn("Validation exeption in method - addDriverToTruck(..)", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return e.getMessage();
        }
    }
}
