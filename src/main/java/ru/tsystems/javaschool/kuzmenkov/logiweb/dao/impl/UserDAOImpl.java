package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import org.springframework.stereotype.Repository;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.UserDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.User;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Nikolay on 22.11.2015.
 */
@Repository
public class UserDAOImpl extends AbstractDAOImpl<User> implements UserDAO {

    @PersistenceContext
    EntityManager entityManager;

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
