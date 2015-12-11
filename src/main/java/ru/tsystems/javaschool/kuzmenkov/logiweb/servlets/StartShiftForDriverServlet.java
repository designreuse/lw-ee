package ru.tsystems.javaschool.kuzmenkov.logiweb.servlets;

import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebAppResources;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Nikolay Kuzmenkov.
 */
public class StartShiftForDriverServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DriverService driverService = LogiwebAppResources.getInstance().getDriverService();

        try {
            Integer driverId = Integer.parseInt(req.getParameter("driverId"));
            driverService.startShiftForDriver(driverId);
            resp.sendRedirect(req.getContextPath() + "/private/driver/DriverPage?driverId=" + driverId);

        } catch (LogiwebServiceException e) {
            e.printStackTrace();
        } catch (LogiwebValidationException e) {
            e.printStackTrace();
        }
    }
}
