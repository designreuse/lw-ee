package ru.tsystems.javaschool.kuzmenkov.logiweb.services.Impl;

import org.apache.log4j.Logger;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.UserDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.User;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.UserService;

import javax.persistence.EntityManager;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Nikolay on 22.11.2015.
 */
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    private EntityManager entityManager;
    private UserDAO userDAO;

    public UserServiceImpl(EntityManager entityManager, UserDAO userDAO) {
        this.entityManager = entityManager;
        this.userDAO = userDAO;
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) throws LogiwebServiceException {
        User user;

        try {
            entityManager.getTransaction().begin();
            user = userDAO.getUserByEmailAndPassword(email, getMD5Hash(password));
            entityManager.getTransaction().commit();

        } catch (LogiwebDAOException e) {
            System.out.println("Exception in UserServiceImpl");
            throw new LogiwebServiceException(e);

        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }

        return user;
    }

    private String getMD5Hash(String md5) throws LogiwebServiceException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            LOGGER.warn("MD5 hashing failed", e);
            throw new LogiwebServiceException("MD5 hashing failed", e);
        }
    }
}
