package ru.tsystems.javaschool.kuzmenkov.logiweb.dao;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.User;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.Role;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;

/**
 * @author Nikolay Kuzmenkov.
 */
public interface UserDAO extends AbstractDAO<User> {

    User findUserByEmail(String userEmail) throws LogiwebDAOException;

    Integer createNewUser(String userEmail, String userPassword, Role userRole) throws LogiwebServiceException, //
            LogiwebValidationException;
}
