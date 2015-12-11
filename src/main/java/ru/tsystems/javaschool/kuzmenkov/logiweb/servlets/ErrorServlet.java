package ru.tsystems.javaschool.kuzmenkov.logiweb.servlets;

import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Nikolay Kuzmenkov.
 */
public class ErrorServlet {

    private static final Logger LOGGER = Logger.getLogger(ErrorServlet.class);

    public static void handleError(HttpServletRequest req, HttpServletResponse resp, Exception e) throws ServletException, IOException {
        LOGGER.error(e);
        e.printStackTrace();

        req.setAttribute("error", e);
        RequestDispatcher rd = req.getRequestDispatcher("/Error.jsp");
        rd.forward(req, resp);
    }
}
