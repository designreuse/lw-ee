package ru.tsystems.javaschool.kuzmenkov.logiweb.servlets;

import org.apache.log4j.Logger;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebAppResources;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.OrderService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.TruckService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Nikolay Kuzmenkov.
 */
public class DeleteDriversAndTruckFromOrderServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(DeleteDriversAndTruckFromOrderServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrderService orderService = LogiwebAppResources.getInstance().getOrderService();
        TruckService truckService = LogiwebAppResources.getInstance().getTruckService();

        int orderId;
        try {
            orderId = Integer.parseInt(req.getParameter("orderId"));

            Order order = orderService.findOrderById(orderId);
            Truck truck = order.getAssignedTruckFK();

            if (truck != null) {
                truckService.removeAssignedOrderAndDriversFromTruck(truck);
            }

            resp.sendRedirect(req.getContextPath() + "/private/manager/EditOrder?orderId=" + orderId);

        } catch (LogiwebServiceException e) {
            LOGGER.warn(e);
            ErrorServlet.handleError(req, resp, e);
        } catch (LogiwebValidationException e) {
            LOGGER.warn(e);
            ErrorServlet.handleError(req, resp, e);
        }
    }
}
