package ru.tsystems.javaschool.kuzmenkov.logiweb.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.TruckDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.TruckStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.RecordNotFoundException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.TruckService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.CitiesUtil;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
@Controller
public class TruckController {

    private static final Logger LOGGER = Logger.getLogger(OrderController.class);

    @Autowired
    private TruckService truckService;
    @Autowired
    private CitiesUtil citiesUtil;

    @RequestMapping(value = {"truck"})
    public String showTrucks(Model model) {
        List<TruckDTO> trucks = truckService.findAllTrucks();
        model.addAttribute("trucks", trucks);
        citiesUtil.addAllCitiesToModel(model);

        return "truck/TruckList";
    }

    @RequestMapping(value = {"truck/new"}, method = RequestMethod.GET)
    public String showFormForNewTruck(Model model) {
        model.addAttribute("formAction", "new");
        model.addAttribute("truckFromForm", new TruckDTO());
        citiesUtil.addAllCitiesToModel(model);

        return "truck/AddOrEditTruck";
    }

    @RequestMapping(value = {"truck/{truckId}/edit"}, method = RequestMethod.GET)
    public String showFormForEditDriver(@PathVariable("truckId") Integer truckId, Model model) {
        model.addAttribute("formAction", "edit");

        TruckDTO truckToEdit = truckService.findTruckById(truckId);

        if (truckToEdit == null) {
            throw new RecordNotFoundException();
        }

        model.addAttribute("truckFromForm", truckToEdit);
        citiesUtil.addAllCitiesToModel(model);
        model.addAttribute("truckStatuses", TruckStatus.values());

        return "truck/AddOrEditTruck";
    }

    @RequestMapping(value = {"truck/new"}, method = RequestMethod.POST)
    public String addTruck(@ModelAttribute("truckFromForm") TruckDTO newTruckFromForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("truckFromForm", newTruckFromForm);
            model.addAttribute("formAction", "new");
            citiesUtil.addAllCitiesToModel(model);

            return "truck/AddOrEditTruck";
        }

        try {
            truckService.addNewTruck(newTruckFromForm);

            return "redirect:/truck";

        } catch (LogiwebValidationException e) {
            LOGGER.warn("Validation exception in method - addTruck(..)", e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("formAction", "new");
            citiesUtil.addAllCitiesToModel(model);
            return "truck/AddOrEditTruck";
        }
    }

    @RequestMapping(value = {"truck/{truckId}/edit"}, method = RequestMethod.POST)
    public String editTruck(
            @ModelAttribute("truckFromForm") @Valid TruckDTO editTruckFromForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("truckFromForm", editTruckFromForm);
            citiesUtil.addAllCitiesToModel(model);
            model.addAttribute("formAction", "edit");

            return "truck/AddOrEditTruck";
        }

        try {
            truckService.editTruck(editTruckFromForm);

            return "redirect:/truck";

        } catch (LogiwebValidationException e) {
            LOGGER.warn("Validation exception in method - editTruck(..)", e);
            model.addAttribute("error", e.getMessage());
            citiesUtil.addAllCitiesToModel(model);
            model.addAttribute("formAction", "edit");
            return "truck/AddOrEditTruck";
        }
    }

    /**
     * Removes truck by its ID received in 'truckId' parameter.
     *
     * @param truckId
     * @param response
     * @return
     */
    @RequestMapping(value = "truck/{truckId}/delete", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String deleteTruck(@PathVariable("truckId") Integer truckId, HttpServletResponse response) {
        try {
            truckService.removeTruck(truckId);

            return "Driver deleted";

        } catch (LogiwebValidationException e) {
            LOGGER.warn("Validation exception in method - deleteTruck(..)", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return e.getMessage();
        }
    }
}
