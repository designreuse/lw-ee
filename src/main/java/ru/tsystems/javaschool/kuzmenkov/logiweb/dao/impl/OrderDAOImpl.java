package ru.tsystems.javaschool.kuzmenkov.logiweb.dao.impl;

import org.springframework.stereotype.Repository;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.OrderDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;

/**
 * @author Nikolay Kuzmenkov.
 */
@Repository("orderDAO")
public class OrderDAOImpl extends AbstractDAOImpl<Order> implements OrderDAO {

}
