package ru.tsystems.javaschool.kuzmenkov.logiweb.services;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Freight;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;

import java.util.List;

/**
 * Created by Nikolay on 20.11.2015.
 */
public interface OrderService {

    List<Order> findAllOrders() throws LogiwebServiceException;

    List<Freight> findAllFreights() throws LogiwebServiceException;

    Order addNewOrder(Order newOrder) throws LogiwebServiceException;

    Order findOrderById(Integer orderId) throws LogiwebServiceException;

    void addNewFreight(Freight newFreight) throws LogiwebServiceException, LogiwebValidationException;

    void assignTruckToOrder(Truck assignedTruck, Integer orderId) throws LogiwebServiceException, LogiwebValidationException;

    void setReadyStatusForOrder(Order order) throws LogiwebServiceException, LogiwebValidationException;
}
