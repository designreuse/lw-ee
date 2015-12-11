package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.UserDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.User;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Nikolay on 22.11.2015.
 */
public class UserDAOImpl extends AbstractDAOImpl<User> implements UserDAO {

    public UserDAOImpl(Class<User> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) throws LogiwebDAOException {
        List<User> queryResult;
        try {
            Query query = getEntityManager().createQuery("SELECT u FROM User u WHERE u.userEmail = :userEmail " +
                    "AND u.userPassword = :userPassword");
            query.setParameter("userEmail", email);
            query.setParameter("userPassword", password);

            queryResult = query.getResultList();

            if(queryResult.isEmpty()) {
                throw new LogiwebDAOException();
            }

        } catch (Exception e) {
            System.out.println("Exception in UserDAOImpl");
            throw new LogiwebDAOException(e);
        }

        return queryResult.get(0);
    }
}
