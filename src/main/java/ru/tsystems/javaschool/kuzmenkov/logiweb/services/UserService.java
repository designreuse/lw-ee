package ru.tsystems.javaschool.kuzmenkov.logiweb.services;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.User;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;

/**
 * Created by Nikolay on 22.11.2015.
 */
public interface UserService {

    User getUserByEmailAndPassword(String email, String password) throws LogiwebServiceException;
}
