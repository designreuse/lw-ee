package ru.tsystems.javaschool.kuzmenkov.logiweb.entities;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.WayPointStatus;

/**
 * @author Nikolay Kuzmenkov.
 */
public class WayPoint {

    private WayPointStatus wayPointStatus;
    private City city;
    private Freight freight;

    public WayPoint(WayPointStatus wayPointStatus, City city, Freight freight) {
        this.wayPointStatus = wayPointStatus;
        this.city = city;
        this.freight = freight;
    }

    public WayPointStatus getWayPointStatus() {
        return wayPointStatus;
    }

    public City getCity() {
        return city;
    }

    public Freight getFreight() {
        return freight;
    }
}
