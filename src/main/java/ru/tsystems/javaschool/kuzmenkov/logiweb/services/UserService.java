package ru.tsystems.javaschool.kuzmenkov.logiweb.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.User;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.Role;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;

import java.security.NoSuchAlgorithmException;

/**
 * @author Nikolay Kuzmenkov.
 */
public interface UserService extends UserDetailsService {

    Integer createNewUser(String userEmail, String userPassword, Role userRole) throws LogiwebDAOException, LogiwebValidationException, NoSuchAlgorithmException;

    User findUserByEmail(String userEmail) throws LogiwebDAOException;
}
