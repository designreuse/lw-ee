package ru.tsystems.javaschool.kuzmenkov.logiweb.servlets;

import org.apache.log4j.Logger;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebAppResources;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
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
public class AssignTruckToOrderServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AssignTruckToOrderServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrderService orderService = LogiwebAppResources.getInstance().getOrderService();

        Integer orderId = 0;
        Integer truckId = 0;

        try {
            orderId = Integer.parseInt(req.getParameter("orderId"));
            truckId = Integer.parseInt(req.getParameter("truckId"));

        } catch (NumberFormatException | NullPointerException e) {
            LOGGER.warn(e);
            ErrorServlet.handleError(req, resp, e);

        }

        try {
            Truck truck = new Truck();
            truck.setTruckId(truckId);
            orderService.assignTruckToOrder(truck, orderId);

        } catch (LogiwebValidationException e) {
            LOGGER.warn(e);
            ErrorServlet.handleError(req, resp, e);
        } catch (LogiwebServiceException e) {
            LOGGER.warn(e);
            ErrorServlet.handleError(req, resp, e);
        }
    }
}
