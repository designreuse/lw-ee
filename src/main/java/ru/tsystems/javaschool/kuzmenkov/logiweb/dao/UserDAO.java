package ru.tsystems.javaschool.kuzmenkov.logiweb.dao;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.User;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;

/**
 * @author Nikolay Kuzmenkov.
 */
public interface UserDAO extends AbstractDAO<User> {

    User findUserByEmail(String userEmail) throws LogiwebDAOException;
}
