package ru.tsystems.javaschool.kuzmenkov.logiweb.services;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Freight;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.OrderRoute;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebServiceException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;

import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
public interface FreightService {

    void setPickedUpStatus(Integer freightId) throws IllegalStateException, LogiwebServiceException;

    void addNewFreight(Freight newFreight) throws LogiwebServiceException, LogiwebValidationException;

    List<Freight> findAllFreights() throws LogiwebServiceException;

    OrderRoute getRouteInformationForOrder(Integer orderId) throws LogiwebServiceException;
}
