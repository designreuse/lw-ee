package ru.tsystems.javaschool.kuzmenkov.logiweb.servlets;

import org.apache.log4j.Logger;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebAppResources;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.OrderStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.OrderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Nikolay Kuzmekov.
 */
public class AddOrderServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AddOrderServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrderService orderService = LogiwebAppResources.getInstance().getOrderService();

        Order newOrder = new Order();
        Integer orderId;

        try {
            newOrder.setOrderStatus(OrderStatus.CREATED);
            orderService.addNewOrder(newOrder);
            orderId = newOrder.getOrderId();
            resp.sendRedirect(req.getContextPath() + "/private/manager/EditOrder?orderId=" + orderId);

        } catch (LogiwebServiceException e) {
            LOGGER.warn(e);
            ErrorServlet.handleError(req, resp, e);
        }
    }
}
