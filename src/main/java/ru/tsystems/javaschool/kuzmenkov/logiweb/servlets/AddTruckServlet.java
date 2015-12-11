package ru.tsystems.javaschool.kuzmenkov.logiweb.servlets;

import org.apache.log4j.Logger;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebAppResources;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.CityService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.TruckService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Nikolay on 26.11.2015.
 */
public class AddTruckServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AddTruckServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CityService cityService = LogiwebAppResources.getInstance().getCityService();

        try {
            List<City> cities = cityService.findAllCities();
            req.setAttribute("cities", cities);

            RequestDispatcher requestDispatcher = req.getRequestDispatcher("AddTruck.jsp");
            requestDispatcher.forward(req, resp);

        } catch (LogiwebServiceException e) {
            LOGGER.warn(e);
            ErrorServlet.handleError(req, resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TruckService truckService = LogiwebAppResources.getInstance().getTruckService();
        CityService cityService = LogiwebAppResources.getInstance().getCityService();

        try {
            List<City> cities = cityService.findAllCities();
            req.setAttribute("cities", cities);

            Truck newTruck = new Truck();

            Integer driverCount;
            try {
                driverCount = Integer.parseInt(req.getParameter("driverCount"));
            } catch (NumberFormatException | NullPointerException e) {
                throw new LogiwebValidationException("Driver count field is in wrong format.");
            }

            Float capacity;
            try {
                capacity = Float.parseFloat(req.getParameter("capacity"));
            } catch (NumberFormatException | NullPointerException e) {
                throw new LogiwebValidationException("Freight capacity field is in wrong format. Must be like: '1.5'");
            }

            Integer cityId;
            try {
                cityId = Integer.parseInt(req.getParameter("city"));
            } catch (NumberFormatException | NullPointerException e) {
                throw new LogiwebValidationException("City selector must return city ID as integer.");
            }

            newTruck.setTruckNumber(req.getParameter("truckNumber"));
            newTruck.setDriverCount(driverCount);
            newTruck.setCapacity(capacity);
            City city = cityService.findCityById(cityId);
            newTruck.setCurrentCityFK(city);

            truckService.addNewTruck(newTruck);

            resp.sendRedirect(req.getContextPath() + "/private/manager/TruckList");

        } catch (LogiwebValidationException e) {
            req.setAttribute("error", e.getMessage());
            RequestDispatcher rq = req.getRequestDispatcher("AddTruck.jsp");
            rq.forward(req, resp);
        } catch (LogiwebServiceException e) {
            LOGGER.warn(e);
            ErrorServlet.handleError(req, resp, e);
        }
    }
}
