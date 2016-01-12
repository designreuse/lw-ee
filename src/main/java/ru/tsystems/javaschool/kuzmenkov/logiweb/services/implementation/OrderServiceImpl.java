package ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.OrderDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.TruckDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.OrderDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Freight;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.FreightStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.OrderStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.TruckStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.OrderService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.EntityDTODataConverter;

import java.util.List;
import java.util.Set;

/**
 * Data manipulation and business logic related to Freights and Orders.
 *
 * @author Nikolay Kuzmenkov.
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = Logger.getLogger(OrderServiceImpl.class);

    @Autowired
    private EntityDTODataConverter converter;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private TruckDAO truckDAO;

    /**
     * Find all orders.
     *
     * @return orders set.
     * @throws LogiwebDAOException
     *             if something unexpected happens
     */
    @Override
    @Transactional
    public List<Order> findAllOrders() throws LogiwebDAOException {
        return orderDAO.findAll();
    }

    /**
     * Create new empty order with Not Ready status.
     *
     * @return ID of created order
     * @throws LogiwebDAOException
     *             if something unexpected happens
     */
    @Override
    @Transactional
    public Integer addNewOrder() throws LogiwebDAOException {
        Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatus.CREATED);
        orderDAO.create(newOrder);

        LOGGER.info("Order created. ID#" + newOrder.getOrderId());

        return newOrder.getOrderId();
    }

    /**
     * Find order by id.
     *
     * @param orderId
     * @return order DTO or null if not found.
     * @throws LogiwebDAOException
     *             if something unexpected happens
     */
    @Override
    @Transactional
    public OrderDTO findOrderById(Integer orderId) throws LogiwebDAOException {
        Order order = orderDAO.findById(orderId);
        return converter.convertOrderEntityToDTO(order);

    }

    /**
     * Check if all freights were delivered and order can be set to Delivered.
     *
     * @param orderId
     * @return true if order complete and false if there are undelivered freights
     *         inside order.
     */
    @Override
    @Transactional
    public boolean isAllFreightsInOrderDelivered(Integer orderId) {
            Order order = orderDAO.findById(orderId);

            Set<Freight> freights = order.getOrderLines();

            for (Freight freight : freights) {
                if (freight.getFreightStatus() != FreightStatus.DELIVERED) {
                    return false;
                }
            }
            return true;
    }

    /**
     * Sets 'DELIVERED' status for order if all freights for this order were
     * delivered.
     * Unassign truck and drivers from order.
     *
     * @param orderId
     *
     */
    @Override
    @Transactional
    public void setStatusDeliveredForOrder(Integer orderId) throws LogiwebValidationException {
        Order order = orderDAO.findById(orderId);


        if (isAllFreightsInOrderDelivered(orderId)) {
            order.setOrderStatus(OrderStatus.DELIVERED);
            orderDAO.update(order);
            LOGGER.info("Order id#" + order.getOrderId() + " changed status to "
                    + OrderStatus.DELIVERED);
        } else {
            throw new LogiwebValidationException(
                    "Order have undelivered freight.");
        }
    }
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
    @Override
    @Transactional
    public void assignTruck(Integer assignedTruckId, Integer orderId) throws LogiwebDAOException, LogiwebValidationException {
        Truck assignedTruck = truckDAO.findById(assignedTruckId);

        if(assignedTruck.getOrderForThisTruck() != null) {
            throw new LogiwebValidationException("Selected truck must have no assigned orders.");
        }

        Order orderForTruck = orderDAO.findById(orderId);

        if (orderForTruck.getAssignedTruckFK() != null) {
            throw new LogiwebValidationException("Order with ID " + orderId + " must not be assigned to another truck.");
        }
        else if (orderForTruck.getOrderLines() == null || orderForTruck.getOrderLines().isEmpty()) {
            throw new LogiwebValidationException("Order with ID " + orderId + " must have freight.");
        }

        assignedTruck.setOrderForThisTruck(orderForTruck);
        orderForTruck.setAssignedTruckFK(assignedTruck);
        orderDAO.update(orderForTruck);
        truckDAO.update(assignedTruck);

        LOGGER.info("Truck with ID" + assignedTruck.getTruckId() + " assign to order " + orderForTruck.getOrderId());
    }

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
    @Override
    @Transactional
    public void setReadyStatusForOrder(Integer orderId) throws LogiwebDAOException, LogiwebValidationException {
        Order order = orderDAO.findById(orderId);

        if (order.getOrderLines() == null || order.getOrderLines().isEmpty()) {
            throw new LogiwebValidationException("Order must contain at least 1 cargo.");
        }
        else if (order.getAssignedTruckFK() == null) {
            throw new LogiwebValidationException("Order must have assigned truck.");
        }
        else if (order.getAssignedTruckFK().getDriversInTruck() == null || order.getAssignedTruckFK().getDriversInTruck().size()
                < order.getAssignedTruckFK().getDriverCount()) {
            throw new LogiwebValidationException("Truck must have full count of drivers. Assign drivers.");
        }
        else if (order.getOrderStatus() != OrderStatus.CREATED) {
            throw new LogiwebValidationException("Order must be in 'CREATED' state.");
        }

        order.setOrderStatus(OrderStatus.READY_TO_GO);

        orderDAO.update(order);
        LOGGER.info("Order id#" + order.getOrderId() + " changed status to " + OrderStatus.READY_TO_GO);
    }
}
