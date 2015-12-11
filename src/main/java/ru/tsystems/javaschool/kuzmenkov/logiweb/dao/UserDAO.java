package ru.tsystems.javaschool.kuzmenkov.logiweb.dao;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.User;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;

/**
 * Created by Nikolay on 22.11.2015.
 */
public interface UserDAO extends AbstractDAO<User> {

    User getUserByEmailAndPassword(String email, String password) throws LogiwebDAOException;
}
