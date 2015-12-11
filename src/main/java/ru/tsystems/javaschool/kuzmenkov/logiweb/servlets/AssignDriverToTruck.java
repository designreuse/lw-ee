package ru.tsystems.javaschool.kuzmenkov.logiweb.servlets;

import org.apache.log4j.Logger;
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
public class AssignDriverToTruck extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AssignDriverToTruck.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DriverService driverService = LogiwebAppResources.getInstance().getDriverService();

        String[] driverIdsStrings = req.getParameterValues("driversIds");
        if(driverIdsStrings == null || driverIdsStrings.length == 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        int[] driversIds = new int[driverIdsStrings.length];
        try {
            for (int i = 0; i < driverIdsStrings.length; i++) {
                driversIds[i] = Integer.parseInt(driverIdsStrings[i]);
            }

            Integer truckId = Integer.parseInt(req.getParameter("truckId"));

            for (int driverId : driversIds) {
                driverService.assignDriverToTruck(driverId, truckId);
            }

        } catch (NumberFormatException e) {
            LOGGER.warn(e);
            ErrorServlet.handleError(req, resp, e);
        } catch (LogiwebValidationException e) {
            LOGGER.warn(e);
            ErrorServlet.handleError(req, resp, e);
        } catch (LogiwebServiceException e) {
            LOGGER.warn(e);
            ErrorServlet.handleError(req, resp, e);
        }
    }
}
