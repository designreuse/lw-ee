package ru.tsystems.javaschool.kuzmenkov.logiweb.services.Impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.UserDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.User;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.Role;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Nikolay Kuzmenkov.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserDAO userDAO;

    @Override
    @Transactional
    public Integer createNewUser(String userEmail, String userPassword, Role userRole) throws LogiwebServiceException, LogiwebValidationException {
        if (userEmail == null || userEmail.isEmpty()) {
            throw new LogiwebValidationException(
                    "Username can't be empty.");
        }

        try {
            User userWithSameMail = userDAO.findUserByEmail(userEmail);
            if (userWithSameMail != null) {
                throw new LogiwebValidationException(
                        "User with email: " + userEmail + " already exist.");
            }

            User newUser = new User();
            newUser.setUserEmail(userEmail);
            newUser.setUserPassword(getMD5Hash(userPassword));
            newUser.setUserRole(userRole);
            userDAO.create(newUser);

            LOGGER.info("User #" + newUser.getUserId() + " " + userEmail + " created");

            return newUser.getUserId();

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        }
    }

    @Override
    @Transactional
    public User findUserById(Integer userId) throws LogiwebServiceException {
        try {
            return userDAO.findById(userId);

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Something unexcpected happend.");
            throw new LogiwebServiceException(e);
        }
    }

    private String getMD5Hash(String userPassword) throws LogiwebServiceException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            byte[] array = md.digest(userPassword.getBytes());
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            LOGGER.warn("MD5 hashing failed", e);
            throw new LogiwebServiceException("MD5 hashing failed", e);
        }
    }
}
