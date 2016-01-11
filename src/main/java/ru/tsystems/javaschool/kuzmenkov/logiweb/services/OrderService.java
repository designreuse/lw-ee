package ru.tsystems.javaschool.kuzmenkov.logiweb.services;

import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.OrderDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;

import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
public interface OrderService {

    List<Order> findAllOrders() throws LogiwebDAOException;

    Integer addNewOrder() throws LogiwebDAOException;

    OrderDTO findOrderById(Integer orderId) throws LogiwebDAOException;

    void assignTruck(Integer assignedTruckId, Integer orderId) throws LogiwebDAOException, LogiwebValidationException;

    void setReadyStatusForOrder(Integer orderId) throws LogiwebDAOException, LogiwebValidationException;
}
