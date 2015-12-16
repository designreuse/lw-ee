package ru.tsystems.javaschool.kuzmenkov.logiweb.services.Impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.CityDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.FreightDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.OrderDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.TruckDAO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Freight;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.FreightStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.OrderStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.TruckStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.OrderService;
import ru.tsystems.javaschool.kuzmenkov.logiweb.util.LogiwebValidator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = Logger.getLogger(OrderServiceImpl.class);
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CityDAO cityDAO;
    @Autowired
    private FreightDAO freightDAO;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private TruckDAO truckDAO;

    @Override
    public List<Order> findAllOrders() throws LogiwebServiceException {
        List<Order> allOrdersResult;

        try {
            entityManager.getTransaction().begin();
            allOrdersResult = orderDAO.findAll();
            entityManager.getTransaction().commit();

            return allOrdersResult;

        } catch(LogiwebDAOException e) {
            System.out.println("Exception in OrderServiceImpl");
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public List<Freight> findAllFreights() throws LogiwebServiceException {
        List<Freight> allFreightsResult;

        try {
            entityManager.getTransaction().begin();
            allFreightsResult = freightDAO.findAll();
            entityManager.getTransaction().rollback();

            return allFreightsResult;

        } catch(LogiwebDAOException e) {
            System.out.println("Exception in OrderServiceImpl");
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public Order addNewOrder(Order newOrder) throws LogiwebServiceException {
        try {
            entityManager.getTransaction().begin();
            orderDAO.create(newOrder);
            entityManager.getTransaction().commit();

            return newOrder;

        } catch(LogiwebDAOException e) {
            System.out.println("Exception in OrderServiceImpl");
            throw new LogiwebServiceException(e);
        } finally {
            if(entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public Order findOrderById(Integer orderId) throws LogiwebServiceException {
        Order orderResult;

        try {
            entityManager.getTransaction().begin();
            orderResult = orderDAO.findById(orderId);
            entityManager.getTransaction().commit();

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in OrderServiceImpl - findOrderById().", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }

        return orderResult;
    }

    @Override
    public void addNewFreight(Freight newFreight) throws LogiwebServiceException,LogiwebValidationException {
        LogiwebValidator.validateFreightFormValues(newFreight);

        try {
            entityManager.getTransaction().begin();

            //get managed entities
            City cityFrom = cityDAO.findById(newFreight.getCityFromFK().getCityId());
            City cityTo = cityDAO.findById(newFreight.getCityToFK().getCityId());

            Order orderForFreight = orderDAO.findById(newFreight.getOrderForThisFreightFK().getOrderId());

            //switch detached entities in cargo to managed ones
            newFreight.setCityFromFK(cityFrom);
            newFreight.setCityToFK(cityTo);
            newFreight.setOrderForThisFreightFK(orderForFreight);

            newFreight.setFreightStatus(FreightStatus.WAITING_FOR_PICK_UP);

            freightDAO.create(newFreight);
            LOGGER.info("New cargo with id #" + newFreight.getFreightId() + "created for irder id #" + orderForFreight.getOrderId());
            entityManager.refresh(newFreight.getOrderForThisFreightFK());
            entityManager.getTransaction().commit();

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public void assignTruckToOrder(Truck assignedTruck, Integer orderId) throws LogiwebServiceException,
            LogiwebValidationException {

        try {
            if(assignedTruck == null) {
                throw new LogiwebValidationException("Selected truck does not exist.");
            }

            entityManager.getTransaction().begin();
            assignedTruck = truckDAO.findById(assignedTruck.getTruckId());

            if(assignedTruck == null) {
                throw new LogiwebValidationException("Selected truck does not exist.");
            }
            else if(assignedTruck.getTruckStatus() != TruckStatus.WORKING) {
                throw new LogiwebValidationException("Selected truck must have TruckStatus 'WORKING'.");
            }
            else if(assignedTruck.getOrderForThisTruck() != null) {
                throw new LogiwebValidationException("Selected truck must have no assigned orders.");
            }

            Order orderForTruck = orderDAO.findById(orderId);

            if(orderForTruck == null) {
                throw new LogiwebValidationException("Order with ID " + orderId + " does not exist.");
            }
            else if (orderForTruck.getAssignedTruckFK() != null) {
                throw new LogiwebValidationException("Order with ID " + orderId + " must not be assigned to another truck.");
            }
            else if (orderForTruck.getOrderLines() == null || orderForTruck.getOrderLines().isEmpty()) {
                throw new LogiwebValidationException("Order with ID " + orderId + " must have freight.");
            }

            assignedTruck.setOrderForThisTruck(orderForTruck);
            orderForTruck.setAssignedTruckFK(assignedTruck);
            orderDAO.update(orderForTruck);
            truckDAO.update(assignedTruck);

            System.out.println("Truck with ID" + assignedTruck.getTruckId() + " assign to order " + orderForTruck.getOrderId());

            entityManager.getTransaction().commit();

        } catch(LogiwebDAOException e) {
            System.out.println("Exception in OrderServiceImpl");
            throw new LogiwebServiceException(e);
        } finally {
            if(entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public void setReadyStatusForOrder(Order order) throws LogiwebValidationException, LogiwebServiceException {
        if(order == null) {
            throw new LogiwebValidationException("Order does not exist.");
        }
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

        try {
            entityManager.getTransaction().begin();
            order.setOrderStatus(OrderStatus.READY_TO_GO);
            orderDAO.update(order);

            LOGGER.info("Order id#" + order.getOrderId() + " changed status to " + OrderStatus.READY_TO_GO);

            entityManager.getTransaction().commit();

        } catch (LogiwebDAOException e) {
            LOGGER.warn("Exception in OrderServiceImpl - setReadyStatusForOrder().", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }
}
