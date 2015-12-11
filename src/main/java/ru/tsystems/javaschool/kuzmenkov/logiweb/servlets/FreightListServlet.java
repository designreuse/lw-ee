package ru.tsystems.javaschool.kuzmenkov.logiweb.servlets;

import org.apache.log4j.Logger;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebAppResources;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Freight;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.OrderService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
public class FreightListServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(FreightListServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrderService orderService = LogiwebAppResources.getInstance().getOrderService();
        List<Freight> freights;

        try {
            freights = orderService.findAllFreights();
            req.setAttribute("freights", freights);

            RequestDispatcher rd = req.getRequestDispatcher("FreightList.jsp");
            rd.forward(req, resp);

        } catch (LogiwebServiceException e) {
            LOGGER.warn("Exception in ShowSingleDriverServlet", e);
            ErrorServlet.handleError(req, resp, e);
        } catch (Exception e) {
            LOGGER.warn(e);
            ErrorServlet.handleError(req, resp, e);
        }
    }
}
