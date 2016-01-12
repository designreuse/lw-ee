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

    /**
     * Check if all freights were delivered and order can be set to Delivered.
     *
     * @param orderId
     * @return true if order complete and false if there are undelivered freights
     *         inside order.
     */
    boolean isAllFreightsInOrderDelivered(Integer orderId);

    /**
     * Sets 'DELIVERED' status for order if all freights for this order were
     * delivered.
     * Unassign truck and drivers from order.
     *
     * @param orderId
     *
     */
    void setStatusDeliveredForOrder(Integer orderId) throws LogiwebValidationException;
}
