package ru.tsystems.javaschool.kuzmenkov.logiweb.entities;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.WayPointStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Nikolay Kuzmenkov.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WayPoint {

    @Enumerated(EnumType.STRING)
    private WayPointStatus wayPointStatus;
    @XmlTransient
    private City city;
    private Freight freight;

    public WayPoint() {
        // Default constructor without parameters.
    }

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
