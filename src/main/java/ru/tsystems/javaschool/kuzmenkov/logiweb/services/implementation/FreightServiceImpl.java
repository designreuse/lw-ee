package ru.tsystems.javaschool.kuzmenkov.logiweb.services.implementation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dao.*;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.*;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.FreightStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.OrderStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.WayPointStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;
import ru.tsystems.javaschool.kuzmenkov.logiweb.services.FreightService;

import java.util.*;

/**
 * @author Nikolay Kuzmenkov.
 */
@Service("freightService")
public class FreightServiceImpl implements FreightService {

    private static final Logger LOGGER = Logger.getLogger(FreightServiceImpl.class);

    /**
     * Used for generation of random delivery time.
     * Each cargo in order adds some time to total delivery time.
     * This sets minimal limit for each cargo.
     */
    private static final float MIN_DELIVERY_TIME = 10;

    /**
     * Used for generation of random delivery time.
     * Each cargo in order adds some time to total delivery time.
     * This sets max limit for each cargo.
     */
    private static final float MAX_DELIVERY_TIME = 20;

    @Autowired
    private DriverDAO driverDAO;
    @Autowired
    private FreightDAO freightDAO;
    @Autowired
    private CityDAO cityDAO;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private TruckDAO truckDAO;

    @Override
    @Transactional
    public void addNewFreight(Freight newFreight) throws LogiwebDAOException, LogiwebValidationException {
        //get managed entities
        City cityFrom = cityDAO.findById(newFreight.getCityFromFK().getCityId());
        City cityTo = cityDAO.findById(newFreight.getCityToFK().getCityId());

        Order orderForFreight = orderDAO.findById(newFreight.getOrderForThisFreightFK().getOrderId());

        if(orderForFreight.getAssignedTruckFK() != null) {
            throw new LogiwebValidationException("Can't add freight to Order which already have assigned Truck.");
        }

        //switch detached entities in cargo to managed ones
        newFreight.setCityFromFK(cityFrom);
        newFreight.setCityToFK(cityTo);
        newFreight.setOrderForThisFreightFK(orderForFreight);

        newFreight.setFreightStatus(FreightStatus.WAITING_FOR_PICK_UP);

        freightDAO.create(newFreight);
        LOGGER.info("New freight with ID# " + newFreight.getFreightId() + "created for order ID#"
                + orderForFreight.getOrderId());
    }

    /**
     * Find all freights.
     *
     * @return cargoes set or empty set.
     * @throws LogiwebDAOException
     *             if something unexpected happens
     */
    @Override
    @Transactional
    public List<Freight> findAllFreights() throws LogiwebDAOException {
            return freightDAO.findAll();
    }

    @Override
    @Transactional
    public Freight findFreightById(Integer freightId) {
        return freightDAO.findById(freightId);
    }

    @Override
    @Transactional
    public OrderRoute getRouteInformationForOrder(Integer orderId) throws LogiwebDAOException {
        Order order = orderDAO.findById(orderId);

        if (order == null) {
            return null;
        }

        return new OrderRoute(getPseudoRandomFloatBasedOnFreightsInOrder(order), getTotalWeightOfAllFreightsInOrder(order),
                getCitiesFromBeforeCitiesTo(order));
    }

    @Override
    @Transactional
    public void setPickUpStatus(Integer freightId) throws IllegalStateException, LogiwebDAOException {
        Freight freight = freightDAO.findById(freightId);

        if (freight == null) {
            throw new IllegalStateException();
        }

        if (freight.getOrderForThisFreightFK().getOrderStatus() != OrderStatus.READY_TO_GO) {
            throw new IllegalStateException("Order for freight must be in 'Ready to go' state");
        }

        if (freight.getFreightStatus() != FreightStatus.WAITING_FOR_PICK_UP) {
            throw new IllegalStateException("Freight must be in 'Waiting for pickup' state");
        }

        freight.setFreightStatus(FreightStatus.PICKED_UP);
        freightDAO.update(freight);
    }

    @Override
    @Transactional
    public void setDeliverStatus(Integer freightId, Integer driverPersonalNumber) throws LogiwebDAOException {
        Freight freight = freightDAO.findById(freightId);


        if (freight.getOrderForThisFreightFK().getOrderStatus() != OrderStatus.READY_TO_GO) {
            throw new IllegalStateException(
                    "Order for freight must be in 'Ready to go' state");
        }

        if (freight.getFreightStatus() != FreightStatus.PICKED_UP) {
            throw new IllegalStateException(
                    "Freight must be in 'Waiting for pickup' state");
        }

        freight.setFreightStatus(FreightStatus.DELIVERED);
        Order order = freight.getOrderForThisFreightFK();
        Truck truck = truckDAO.findById(order.getAssignedTruckFK().getTruckId());
        truck.setCurrentCityFK(freight.getCityToFK());
        Driver driver = driverDAO.findDriverByPersonalNumber(driverPersonalNumber);
        driver.setCurrentCityFK(freight.getCityToFK());

        freightDAO.update(freight);
        truckDAO.update(truck);
        driverDAO.update(driver);
    }

    private float getPseudoRandomFloatBasedOnFreightsInOrder(Order order) {
        Set<Freight> freightsList = order.getOrderLines();

        if(freightsList == null) {
            return 0f;
        }

        float resultTime = 0f;
        Random rand = new Random();

        for (Freight freight : freightsList) {
            if (freight.getFreightStatus() == FreightStatus.DELIVERED) {
                continue;
            }
            rand.setSeed(freight.getCityToFK().getName().hashCode());
            resultTime += rand.nextFloat() * (MAX_DELIVERY_TIME - MIN_DELIVERY_TIME) + MIN_DELIVERY_TIME;
        }

        return resultTime;
    }

    private float getTotalWeightOfAllFreightsInOrder(Order order) {
        float totalWeight = 0f;
        Set<Freight> freightList = order.getOrderLines();

        if (freightList == null) {
            return 0f;
        }
        for (Freight freight : freightList) {
            totalWeight += freight.getWeight();
        }

        return totalWeight;
    }

    /**
     * Make ordered list of waypoints where origin cities for all cargo are first,
     * then all destination city.
     * Waypoints in each category (deliver and pickup) are ordered.
     * @param order
     * @return
     */
    private List<WayPoint> getCitiesFromBeforeCitiesTo(Order order) {
        List<WayPoint> citiesFromWayPoints = new ArrayList<>();
        List<WayPoint> citiesToWayPoints = new ArrayList<>();
        Set<Freight> freightList = order.getOrderLines();

        if (freightList == null) {
            return new ArrayList<>(0);
        }
        for (Freight freight : freightList) {
            citiesFromWayPoints.add(new WayPoint(WayPointStatus.PICK_UP, freight.getCityFromFK(), freight));
            citiesToWayPoints.add(new WayPoint(WayPointStatus.DELIVER, freight.getCityToFK(), freight));
        }
        /* Anonymous comparator for waypoints. Sort by city id.*/
        Comparator<WayPoint> wpCompareByCityId = new Comparator<WayPoint>() {
            @Override
            public int compare(WayPoint w1, WayPoint w2) {
                return w1.getCity().getCityId() - w2.getCity().getCityId();
            }
        };

        Collections.sort(citiesFromWayPoints, wpCompareByCityId);
        Collections.sort(citiesToWayPoints, wpCompareByCityId);

        List<WayPoint> allWaypointsForThisOrder = new ArrayList<>(citiesFromWayPoints);
        allWaypointsForThisOrder.addAll(citiesToWayPoints);

        return allWaypointsForThisOrder;
    }
}
