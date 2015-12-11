package ru.tsystems.javaschool.kuzmenkov.logiweb.servlets;

import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebAppResources;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.TruckService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Nikolay Kuzmenkov
 */
public class TruckListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TruckService truckService = LogiwebAppResources.getInstance().getTruckService();

        try {
            List<Truck> drivers = truckService.findAllTrucks();
            req.setAttribute("trucks", drivers);

            RequestDispatcher rd = req.getRequestDispatcher("TruckList.jsp");
            rd.forward(req, resp);

        } catch (LogiwebServiceException e) {
            //LOG.warn(e);
            throw new RuntimeException("Unrecoverable server exception.", e);
        }
    }
}