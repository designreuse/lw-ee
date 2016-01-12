package ru.tsystems.javaschool.kuzmenkov.logiweb.services;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Freight;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.OrderRoute;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;

import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
public interface FreightService {

    void setPickUpStatus(Integer freightId) throws IllegalStateException, LogiwebDAOException;

    void setDeliverStatus(Integer freightId, Integer driverPersonalNumber) throws LogiwebDAOException;

    void addNewFreight(Freight newFreight) throws LogiwebDAOException, LogiwebValidationException;

    List<Freight> findAllFreights() throws LogiwebDAOException;

    OrderRoute getRouteInformationForOrder(Integer orderId) throws LogiwebDAOException;

    Freight findFreightById(Integer freightId);
}
