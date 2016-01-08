package ru.tsystems.javaschool.kuzmenkov.logiweb.services;

import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.OrderDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;

import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
public interface OrderService {

    List<Order> findAllOrders() throws LogiwebServiceException;

    Integer addNewOrder() throws LogiwebServiceException;

    OrderDTO findOrderById(Integer orderId) throws LogiwebServiceException;

    void assignTruck(Integer assignedTruckId, Integer orderId) throws LogiwebServiceException;

    void setReadyStatusForOrder(Integer orderId) throws LogiwebServiceException;
}
