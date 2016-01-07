package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import org.springframework.stereotype.Repository;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.UserDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.User;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.Role;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
@Repository("userDAO")
public class UserDAOImpl extends AbstractDAOImpl<User> implements UserDAO {

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
            //LOG.warn(e);
            throw new LogiwebDAOException(e);
        }
    }

    @Override
    public Integer createNewUser(String userEmail, String userPassword, Role userRole) throws LogiwebServiceException, LogiwebValidationException {
        return null;
    }
}
