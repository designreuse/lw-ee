package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import org.springframework.stereotype.Repository;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.OrderDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;

import javax.persistence.EntityManager;

/**
 * Created by Nikolay on 23.11.2015.
 */
@Repository("orderDAO")
public class OrderDAOImpl extends AbstractDAOImpl<Order> implements OrderDAO {

}
