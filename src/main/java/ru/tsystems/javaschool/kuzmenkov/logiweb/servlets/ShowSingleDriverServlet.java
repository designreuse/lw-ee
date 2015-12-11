package ru.tsystems.javaschool.kuzmenkov.logiweb.servlets;

import org.apache.log4j.Logger;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebAppResources;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.OrderRoute;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.DriverService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.RouteInfoForOrder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Nikolay Kuzmenkov.
 */
public class ShowSingleDriverServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ShowSingleDriverServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DriverService driverService = LogiwebAppResources.getInstance().getDriverService();

        try {
            Integer driverId = Integer.parseInt(req.getParameter("driverId"));
            Driver driver = driverService.findDriverById(driverId);

            req.setAttribute("driver", driver);
            req.setAttribute("workingHours", driverService.calculateWorkingHoursForDriver(driver));

            if (driver.getCurrentTruckFK() != null && driver.getCurrentTruckFK().getOrderForThisTruck() != null) {
                OrderRoute orderRouteInfo = RouteInfoForOrder.getRouteInformationForOrder(driver.getCurrentTruckFK()
                        .getOrderForThisTruck());
                req.setAttribute("orderRoute", orderRouteInfo);
            }

            req.setAttribute("shiftRecords", driverService.findDriverShiftRecordsForThisMonth(driver));

            RequestDispatcher rd = req.getRequestDispatcher("SingleDriver.jsp");
            rd.forward(req, resp);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The 'orderId' parameter must not be null, empty or anything other than integer");
        } catch (LogiwebServiceException e) {
            LOGGER.warn("Exception in ShowSingleDriverServlet", e);
            ErrorServlet.handleError(req, resp, e);
        } catch (Exception e) {
            ErrorServlet.handleError(req, resp, e);
        }
    }
}
