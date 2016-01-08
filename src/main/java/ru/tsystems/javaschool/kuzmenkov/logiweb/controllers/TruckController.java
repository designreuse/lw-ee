package ru.tsystems.javaschool.kuzmenkov.logiweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.TruckDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.TruckStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.CityService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.TruckService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.CitiesUtil;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
    @Autowired
    private CitiesUtil citiesUtil;

    @RequestMapping(value = {"truck"})
    public String showTrucks(Model model) throws LogiwebServiceException {
        List<TruckDTO> trucks = truckService.findAllTrucks();
        model.addAttribute("trucks", trucks);
        citiesUtil.addAllCitiesToModel(model);

        return "truck/TruckList";
    }

    @RequestMapping(value = {"truck/new"}, method = RequestMethod.GET)
    public String showFormForNewTruck(Model model) throws LogiwebServiceException {
        model.addAttribute("formAction", "new");
        model.addAttribute("truckFromForm", new TruckDTO());
        citiesUtil.addAllCitiesToModel(model);

        return "truck/AddOrEditTruck";
    }

    @RequestMapping(value = {"truck/{truckId}/edit"}, method = RequestMethod.GET)
    public String showFormForEditDriver(@PathVariable("truckId") Integer truckId, Model model) throws LogiwebServiceException {
        model.addAttribute("formAction", "edit");

        TruckDTO truckToEdit = truckService.findTruckById(truckId);

        if (truckToEdit == null) {
            throw new LogiwebServiceException();
        }

        model.addAttribute("truckFromForm", truckToEdit);
        citiesUtil.addAllCitiesToModel(model);
        model.addAttribute("truckStatuses", TruckStatus.values());

        return "truck/AddOrEditTruck";
    }

    @RequestMapping(value = {"truck/new"}, method = RequestMethod.POST)
    public String addTruck(@ModelAttribute("truckFromForm") TruckDTO newTruckFromForm, BindingResult result, Model model)
            throws LogiwebServiceException {
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
            model.addAttribute("error", e.getMessage());
            model.addAttribute("formAction", "new");
            citiesUtil.addAllCitiesToModel(model);
            return "truck/AddOrEditTruck";
        }
    }

    @RequestMapping(value = {"truck/{truckId}/edit"}, method = RequestMethod.POST)
    public String editTruck(
            @ModelAttribute("truckFromForm") @Valid TruckDTO editTruckFromForm, BindingResult result, Model model)
            throws LogiwebServiceException {
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
     * @throws LogiwebServiceException
     */
    @RequestMapping(value = "truck/{truckId}/delete", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String deleteTruck(@PathVariable("truckId") Integer truckId, HttpServletResponse response)
            throws LogiwebServiceException {
        try {
            truckService.removeTruck(truckId);

            return "Driver deleted";

        } catch (LogiwebValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return e.getMessage();
        }
    }
}
