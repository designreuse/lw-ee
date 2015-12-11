package ru.tsystems.javaschool.kuzmenkov.logiweb.servlets;

import org.apache.log4j.Logger;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebAppResources;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Freight;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.OrderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Nikolay Kuzmenkov.
 */
public class AddFreightServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AddFreightServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrderService orderService = LogiwebAppResources.getInstance().getOrderService();

        try {
            Freight newFreight = createDetachedFreightEntityFromRequestParams(req);

            orderService.addNewFreight(newFreight);

        } catch (LogiwebValidationException e) {
            LOGGER.warn(e);
            ErrorServlet.handleError(req, resp, e);

        } catch (LogiwebServiceException e) {
            LOGGER.warn("Unexpected exception.", e);
            ErrorServlet.handleError(req, resp, e);
        }
    }

    private Freight createDetachedFreightEntityFromRequestParams(HttpServletRequest request) throws LogiwebValidationException {
        int orderId;
        Float freightWeight;

        int originCityId;
        int destinationCityId;

        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new LogiwebValidationException("Order id ( " + request.getParameter("orderId") + ") is in wrong format or null");
        }

        try {
            freightWeight = Float.parseFloat(request.getParameter("cargoWeight"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new LogiwebValidationException("Cargo weight (" + request.getParameter("cargoWeight") + ") is in wrong format or null");
        }

        try {
            originCityId = Integer.parseInt(request.getParameter("originCity"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new LogiwebValidationException("Origin city (" + request.getParameter("originCity") + ") is in wrong format or null");
        }

        try {
            destinationCityId = Integer.parseInt(request.getParameter("destinationCity"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new LogiwebValidationException("Destination city(" + request.getParameter("destinationCity") + ") is in wrong format or null");
        }

        if(destinationCityId == originCityId) {
            throw new LogiwebValidationException();
        }

        City originCity = new City();
        City destinationCity = new City();
        originCity.setCityId(originCityId);
        destinationCity.setCityId(destinationCityId);

        Order orderForThisFreight = new Order();
        orderForThisFreight.setOrderId(orderId);

        Freight newFreight = new Freight();
        newFreight.setOrderForThisFreightFK(orderForThisFreight);
        newFreight.setDescription(request.getParameter("cargoTitle"));
        newFreight.setWeight(freightWeight);
        newFreight.setCityFromFK(originCity);
        newFreight.setCityToFK(destinationCity);

        return newFreight;
    }
}
