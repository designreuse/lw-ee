package ru.tsystems.javaschool.kuzmenkov.logiweb.servlets;

import org.apache.log4j.Logger;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebAppResources;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.User;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.Role;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.UserService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.AuthorizationUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Nikolay on 22.11.2015.
 */
public class AuthServlet extends HttpServlet {

    private static final Logger LOGGGER = Logger.getLogger(AuthServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        UserService userService = LogiwebAppResources.getInstance().getUserService();

        try {
            if (email != null) {
                User user = userService.getUserByEmailAndPassword(email, password);
                AuthorizationUtil.startAuthSessionForUser(req.getSession(), user.getUserRole());
                LOGGGER.info("User id:" + user.getUserId() + " mail:" + user.getUserEmail() + " is logged in.");
                redirectToActionPage(req, resp, user);
            }
        } catch (LogiwebServiceException e) {
            req.setAttribute("error", "User with this pass and mail is not found.");
            RequestDispatcher rq = req.getRequestDispatcher("Login.jsp");
            rq.forward(req, resp);
        }
    }

    private void redirectToActionPage(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        Role userRole= (Role) req.getSession().getAttribute(AuthorizationUtil.SESSION_USER_ROLE_ATTR);

        if (userRole == Role.MANAGER) {
            resp.sendRedirect("private/manager");
        }
        else if (userRole == Role.DRIVER) {
            Integer driverId = user.getAttachedDriver().getDriverId();

            resp.sendRedirect("private/driver/DriverPage?driverId=" + driverId);
        }
    }
}

