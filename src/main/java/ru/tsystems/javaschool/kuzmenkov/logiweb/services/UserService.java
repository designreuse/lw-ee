package ru.tsystems.javaschool.kuzmenkov.logiweb.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.User;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.Role;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;

/**
 * @author Nikolay Kuzmenkov.
 */
public interface UserService extends UserDetailsService {

    Integer createNewUser(String userEmail, String userPassword, Role userRole) throws LogiwebServiceException;

    String getMD5Hash(String userPassword) throws LogiwebServiceException;

    User findUserByEmail(String userEmail) throws LogiwebServiceException;
}
