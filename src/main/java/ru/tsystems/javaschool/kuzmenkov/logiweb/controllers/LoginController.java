package ru.tsystems.javaschool.kuzmenkov.logiweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.User;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.Role;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.UserService;

import java.util.Collection;

/**
 * @author Nikolay Kuzmenkov.
 */
@Controller
public class LoginController {

    /**
     */
    @Autowired
    private UserService userService;

    /**
     * @return Login.jsp
     */
    @RequestMapping(value = "/login")
    public String login() {
        return "main/Login";
    }

    /**
     * @return redirect:/manager or redirect:/driver
     * @throws LogiwebServiceException kk
     */
    @RequestMapping("/")
    public String dispatch() throws LogiwebServiceException {
        Collection<SimpleGrantedAuthority> authorities =
                (Collection<SimpleGrantedAuthority>) SecurityContextHolder.
                        getContext().getAuthentication().getAuthorities();

        GrantedAuthority managerRole = new SimpleGrantedAuthority(
                Role.ROLE_MANAGER.name());
        GrantedAuthority driverRole = new SimpleGrantedAuthority(
                Role.ROLE_DRIVER.name());

        if (authorities.contains(managerRole)) {
            return "redirect:/manager";

        } else if (authorities.contains(driverRole)) {
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User)
                            SecurityContextHolder.getContext().
                                    getAuthentication().getPrincipal();

            User logedInDriver = userService.findUserByEmail(
                    user.getUsername());
            Integer driverId = logedInDriver.getAttachedDriver().getDriverId();
            return "forward:/driver/" + driverId;
        }

        throw new AccessDeniedException("User role is unknown.");
    }
}
