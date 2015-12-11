package ru.tsystems.javaschool.kuzmenkov.logiweb.servlets;

import org.apache.log4j.Logger;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebAppResources;
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
public class ChangeOrderStatusServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ChangeOrderStatusServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("READY CHNAGE");

        OrderService orderService = LogiwebAppResources.getInstance().getOrderService();

        int orderId = 0;

        try {
            orderId = Integer.parseInt(req.getParameter("orderId"));
            Order order = orderService.findOrderById(orderId);
            orderService.setReadyStatusForOrder(order);

            resp.sendRedirect(req.getContextPath() + "/private/manager/EditOrder?orderId=" + orderId);

        } catch (LogiwebValidationException e) {
            LOGGER.warn(e);
            ErrorServlet.handleError(req, resp, e);
        } catch (LogiwebServiceException e) {
            LOGGER.warn(e);
            ErrorServlet.handleError(req, resp, e);
        }
    }
}
