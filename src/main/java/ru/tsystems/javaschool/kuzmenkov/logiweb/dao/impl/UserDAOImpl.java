package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.UserDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.User;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;

import javax.persistence.Query;
import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
@Repository("userDAO")
public class UserDAOImpl extends AbstractDAOImpl<User> implements UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class);

    @Override
    public User findUserByEmail(String userEmail) throws LogiwebDAOException {
        try {
            Query query = getEntityManager().createQuery("SELECT u FROM User u WHERE u.userEmail = :email");
            query.setParameter("email", userEmail);

            List<User> queryResult = query.getResultList();

            if(queryResult != null && !queryResult.isEmpty()) {
                return queryResult.get(0);
            } else {
                return null;
            }

        } catch (Exception e) {
            LOGGER.warn("Exception in UserDAOImpl - findUserByEmail().", e);
            throw new LogiwebDAOException(e);
        }
    }
}
