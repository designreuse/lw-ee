package ru.tsystems.javaschool.kuzmenkov.logiweb.controllers;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.*;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.OrderStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.*;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.CitiesUtil;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * @author Nikolay Kuzmenkov.
 */
@Controller
public class OrderController {

    @Autowired
    private DriverService driverService;
    @Autowired
    private CityService cityService;
    @Autowired
    private FreightService freightService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TruckService truckService;
    @Autowired
    private CitiesUtil citiesUtil;

    private static final float DRIVER_WORKING_HOURS_LIMIT = 176f;

    @RequestMapping(value = {"/order/new"})
    public String addNewOrder() throws LogiwebServiceException {
        Integer newOrderId = orderService.addNewOrder();

        return "redirect:/order/" + newOrderId + "/edit";
    }

    @RequestMapping(value = { "order/{orderId}/edit", "order/{orderId}" }, method = RequestMethod.GET)
    public String editOrder(@PathVariable("orderId") Integer orderId, Model model) throws LogiwebServiceException {
        Order order = orderService.findOrderById(orderId);

        if (order == null) {
            throw new LogiwebServiceException("Order #" + orderId + " not exist.");
        }

        OrderRoute orderRouteInfo = freightService.getRouteInformationForOrder(order.getOrderId());

        model.addAttribute("orderId", orderId);
        model.addAttribute("order", order);
        model.addAttribute("routeInfo", orderRouteInfo);
        model.addAttribute("maxWorkingHoursLimit", DRIVER_WORKING_HOURS_LIMIT);

        //suggest trucks
        if(order.getAssignedTruckFK() == null) {
            List<Truck> suggestedTrucks = truckService.findFreeAndUnbrokenByFreightCapacity(
                    orderRouteInfo.getMaxWeightOnCourse());
            model.addAttribute("suggestedTrucks", suggestedTrucks);
        }

        //suggest drivers
        if(order.getAssignedTruckFK() != null) {
            Float workingHoursLimit = calcMaxWorkingHoursThatDriverCanHave(orderRouteInfo.getEstimatedTime());

            Set<Driver> suggestedDrivers = driverService.findUnassignedDriversByWorkingHoursAndCity(
                    order.getAssignedTruckFK().getCurrentCityFK(), workingHoursLimit);

            for (Driver driver : suggestedDrivers) {
                driver.setWorkingHoursThisMonth(driverService.calculateWorkingHoursForDriver(driver.getDriverId()));
            }

            model.addAttribute("suggestedDrivers", suggestedDrivers);
        }

        model.addAttribute("statuses", OrderStatus.values());

        //citiesUtil.addAllCitiesToModel(model);
        model.addAttribute("cities", cityService.findAllCities());

        return "order/EditOrder";
    }

    private float calcMaxWorkingHoursThatDriverCanHave(float hoursToDeliver) {
        float diff = DateUtil.getHoursUntilEndOfMonth() - hoursToDeliver;

        if(diff < 0 && DRIVER_WORKING_HOURS_LIMIT + diff > 0) {
            return DateUtil.getHoursUntilEndOfMonth();
        } else {
            return DRIVER_WORKING_HOURS_LIMIT - hoursToDeliver;
        }
    }

    @RequestMapping(value = {"/freight"})
    public ModelAndView showCargoes() throws LogiwebServiceException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("freight/FreightList");
        mav.addObject("freights", freightService.findAllFreights());

        return mav;
    }

    @RequestMapping(value = {"/order"})
    public String showOrders(Model model) throws LogiwebServiceException {
        model.addAttribute("orders", orderService.findAllOrders());
        citiesUtil.addAllCitiesToModel(model);

        return "order/OrderList";
    }

    /**
     * Add freight to order.
     *
     * @param request
     * @return
     * @throws LogiwebServiceException
     */
    @RequestMapping(value = "/order/{orderId}/edit/addFreight", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String addFreightToOrder(HttpServletRequest request, HttpServletResponse response)
            throws LogiwebServiceException {
        try {
            Freight newFreight = createDetachedFreightFromRequestParams(request);
            freightService.addNewFreight(newFreight);

            return "Freight added";

        } catch (LogiwebValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return e.getMessage();
        }
    }

    /**
     * Assign truck to order.
     *
     * @param request
     * @return
     * @throws LogiwebServiceException
     */
    @RequestMapping(value = "/order/{orderId}/edit/assignTruck", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String assignTruckToOrder(@PathVariable("orderId") Integer orderId, HttpServletRequest request,
                                     HttpServletResponse response) throws LogiwebServiceException {
        Integer truckId;
        try {
            truckId = Integer.parseInt(request.getParameter("truckId"));

        } catch (NumberFormatException | NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Order and truck id must be equal or more than 0.";
        }

        try {
            orderService.assignTruckToOrder(truckId, orderId);

            return "Truck assigned";

        } catch (LogiwebValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return e.getMessage();
        }
    }

    /**
     * Remove truck and drivers from this order.
     *
     * @param orderId
     * @return
     * @throws LogiwebServiceException
     */
    @RequestMapping(value = "order/{orderId}/edit/removeDriversAndTruck", method = RequestMethod.POST,
            produces = "text/plain")
    @ResponseBody
    public String removeDriversAndTruckFromOrder(@PathVariable("orderId") Integer orderId, HttpServletResponse response)
            throws LogiwebServiceException {
        try {
            Order order = orderService.findOrderById(orderId);
            Truck truck = order.getAssignedTruckFK();

            if(truck != null) {
                truckService.removeAssignedOrderAndDriversFromTruck(truck.getTruckId());

                return "Drivers and Truck relieved from order.";

            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                return "Truck is not assigned";
            }

        } catch (LogiwebValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return e.getMessage();
        }
    }

    @RequestMapping(value = "order/{orderId}/edit/setStatusReady", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String setStatusReady(@PathVariable("orderId") int orderId, HttpServletResponse response)
            throws LogiwebServiceException {
        try {
            orderService.setReadyStatusForOrder(orderId);

            return "Status 'READY' is set";

        } catch (LogiwebValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return e.getMessage();
        }
    }

    private Freight createDetachedFreightFromRequestParams(HttpServletRequest request)
            throws LogiwebValidationException {
        Integer orderId;
        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new LogiwebValidationException("Order ID ( " + request.getParameter("orderId")
                    + ") is in wrong format or null");
        }

        Float freightWeight;
        try {
            freightWeight = Float.parseFloat(request.getParameter("freightWeight"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new LogiwebValidationException("Freight weight (" + request.getParameter("freightWeight")
                    + ") is in wrong format or null");
        }

        Integer originCityId;
        try {
            originCityId = Integer.parseInt(request.getParameter("originCityId"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new LogiwebValidationException("Origin city (" + request.getParameter("originCity")
                    + ") is in wrong format or null");
        }

        Integer destinationCityId;
        try {
            destinationCityId = Integer.parseInt(request.getParameter("destinationCityId"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new LogiwebValidationException("Destination city(" + request.getParameter("destinationCity")
                    + ") is in wrong format or null");
        }

        if(originCityId.equals(destinationCityId)) {
            throw new LogiwebValidationException("Origin city dont be equal destination city");
        }

        Freight newFreight = new Freight();
        newFreight.setDescription(request.getParameter("freightTitle"));

        City originCity = new City();
        City destinationCity = new City();

        originCity.setCityId(originCityId);
        destinationCity.setCityId(destinationCityId);

        newFreight.setCityFromFK(originCity);
        newFreight.setCityToFK(destinationCity);

        Order orderForThisFreight = new Order();
        orderForThisFreight.setOrderId(orderId);
        newFreight.setOrderForThisFreightFK(orderForThisFreight);

        newFreight.setWeight(freightWeight);

        return newFreight;
    }
}
