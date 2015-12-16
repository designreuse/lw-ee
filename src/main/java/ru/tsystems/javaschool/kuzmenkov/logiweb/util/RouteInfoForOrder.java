package ru.tsystems.javaschool.kuzmenkov.logiweb.util;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Freight;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.OrderRoute;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.WayPoint;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.WayPointStatus;

import java.util.*;

/**
 * @author Nikolay Kuzmenkov.
 */
public class RouteInfoForOrder {

    public final static double MAX_WORKING_HOURS_LIMIT = 176;

    /**
     * Used for generation of random delivery time.
     * Each cargo in order adds some time to total delivery time.
     * This sets minimal limit for each cargo.
     */
    private static final double MIN_DELIVERY_TIME = 10;

    /**
     * Used for generation of random delivery time.
     * Each cargo in order adds some time to total delivery time.
     * This sets max limit for each cargo.
     */
    private static final double MAX_DELIVERY_TIME = 20;

    /**
     * Return RouteInformation object, that have random delivery hours (from 10 to 100);
     * order of delivery is sorted by origin first, then destination;
     * weight is maximum weight (all cargo combined).
     */
    public static OrderRoute getRouteInformationForOrder(Order order) {
        OrderRoute info = new OrderRoute(getPseudoRandomFloatBasedOnCargoesInOrder(order),
                getTotalWeightOfAllCargoes(order), getCitiesInOrderOriginBeforeDestination(order));

        return info;
    }

    private static double getPseudoRandomFloatBasedOnCargoesInOrder(Order order) {
        List<Freight> freights = order.getOrderLines();

        if (freights == null) {
            return 0;
        }

        double result = 0;
        Random rand = new Random();

        for(Freight freight : freights) {
            rand.setSeed(freight.hashCode());
            result += rand.nextFloat() * (MAX_DELIVERY_TIME - MIN_DELIVERY_TIME) + MIN_DELIVERY_TIME;
        }

        return result;
    }

    private static Float getTotalWeightOfAllCargoes(Order order) {
        Float totalWeight = 0f;

        List<Freight> freights = order.getOrderLines();

        if(freights == null) {
            return 0f;
        }

        for(Freight freight : freights) {
            totalWeight += freight.getWeight();
        }

        return totalWeight;
    }

    private static List<WayPoint> getCitiesInOrderOriginBeforeDestination(Order order) {
        List<WayPoint> originWayPoints = new ArrayList<WayPoint>();
        List<WayPoint> destinationWayPoints = new ArrayList<WayPoint>();

        List<Freight> freights = order.getOrderLines();

        if(freights == null) {
            return new ArrayList<WayPoint>(0);
        }

        for(Freight freight : freights) {
            originWayPoints.add(new WayPoint(WayPointStatus.PICK_UP, freight.getCityFromFK(), freight));
            destinationWayPoints.add(new WayPoint(WayPointStatus.DELIVER, freight.getCityToFK(), freight));
        }

        /* Anonymous comparator for waypoints. Sort by city id.*/
        Comparator<WayPoint> wpCompareByCityId = new Comparator<WayPoint>() {
            @Override
            public int compare(WayPoint w1, WayPoint w2) {
                return w1.getCity().getCityId() - w2.getCity().getCityId();
            }
        };

        Collections.sort(originWayPoints, wpCompareByCityId);
        Collections.sort(destinationWayPoints, wpCompareByCityId);

        List<WayPoint> allWayPoints = new ArrayList<WayPoint>(originWayPoints);
        allWayPoints.addAll(destinationWayPoints);

        return allWayPoints;
    }
}
