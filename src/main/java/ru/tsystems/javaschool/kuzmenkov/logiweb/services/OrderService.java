package ru.tsystems.javaschool.kuzmenkov.logiweb.services;

import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.OrderDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;

import java.util.List;

/**
 * Data manipulation and business logic related to Freights and Orders.
 *
 * @author Nikolay Kuzmenkov.
 */
public interface OrderService {

    /**
     * Find all orders.
     *
     * @return orders set.
     * @throws LogiwebDAOException
     *             if something unexpected happens
     */
    List<Order> findAllOrders() throws LogiwebDAOException;

    /**
     * Create new empty order with Not Ready status.
     *
     * @return ID of created order
     * @throws LogiwebDAOException
     *             if something unexpected happens
     */
    Integer addNewOrder() throws LogiwebDAOException;

    /**
     * Find order by id.
     *
     * @param orderId
     * @return order DTO or null if not found.
     * @throws LogiwebDAOException
     *             if something unexpected happens
     */
    OrderDTO findOrderById(Integer orderId) throws LogiwebDAOException;

    /**
     * Assign truck to order.
     *
     * @param assignedTruckId
     * @param orderId
     * @throws LogiwebValidationException
     *             if truck is not Free or broken.
     * @throws LogiwebDAOException
     *             if unexpected happened
     */
    void assignTruck(Integer assignedTruckId, Integer orderId) throws LogiwebDAOException, LogiwebValidationException;

    /**
     * Sets 'READY' status for order if order have at least one freight and assign
     * truck with full drivers.
     *
     * @param orderId
     * @throws LogiwebDAOException
     *             if unexpected happened
     * @throws LogiwebValidationException
     *             if validation failed. Description in message.
     */
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
