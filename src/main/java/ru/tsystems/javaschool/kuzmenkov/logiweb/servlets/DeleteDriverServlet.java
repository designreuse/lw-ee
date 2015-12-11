package ru.tsystems.javaschool.kuzmenkov.logiweb.servlets;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebAppResources;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nikolay Kuzmenkov.
 */
public class DeleteDriverServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(DeleteDriverServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("ddd");
        Gson gson = new Gson();
        Map<String, String> jsonMap = new HashMap<String, String>();
        //gson.toJson(jsonMap);
        DriverService driverService = LogiwebAppResources.getInstance().getDriverService();

        try {
            Integer idInt = Integer.parseInt(req.getParameter("driverId"));
            Driver driverToDelete = new Driver();
            driverToDelete.setDriverId(idInt);
            driverService.deleteDriver(driverToDelete);

            //resp.sendRedirect("pri/DriverList");
            //jsonMap.put("msg", "kk");

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            //jsonMap.put("msg", "Can't parse Driver id:" + req.getParameter("driverId") + " to integer.");
        } catch (LogiwebValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            //jsonMap.put("msg", e.getMessage());
        } catch (LogiwebServiceException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOGGER.warn("Unexpected exception.", e);
            //jsonMap.put("msg", "Unexcpected server error. Check logs.");
        }

        //return gson.toJson(jsonMap);
    }
}
