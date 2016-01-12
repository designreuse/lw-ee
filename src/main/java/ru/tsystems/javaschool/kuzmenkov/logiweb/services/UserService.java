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

    /**
     * Create new logiweb user.
     *
     * @param userEmail
     * @param userPassword
     * @param userRole
     * @return
     * @throws LogiwebValidationException if user with this name already exist
     * @throws LogiwebDAOException if something unexpected happened
     */
    Integer createNewUser(String userEmail, String userPassword, Role userRole) throws LogiwebDAOException, LogiwebValidationException, NoSuchAlgorithmException;

    /**
     * Find user by email.
     *
     * @param userEmail
     * @return user
     * @throws LogiwebDAOException if unexpected exception occurred on lower level (not user fault)
     */
    User findUserByEmail(String userEmail) throws LogiwebDAOException;
}
