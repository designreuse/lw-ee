package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.OrderDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;

import javax.persistence.EntityManager;

/**
 * Created by Nikolay on 23.11.2015.
 */
public class OrderDAOImpl extends AbstractDAOImpl<Order> implements OrderDAO {


    public OrderDAOImpl(Class<Order> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);
    }
}
