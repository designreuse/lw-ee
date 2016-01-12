package ru.tsystems.javaschool.kuzmenkov.logiweb.ws;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.WayPoint;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.DriverStatus;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.OrderStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DriverInfo {

    private Integer personalNumber;

    private String firstName;

    private String lastName;

    private Float workingHoursInThisMonth;

    private DriverStatus currentDriverStatus;

    private List<WayPoint> orderWayPoints;

    private OrderStatus orderStatus;

    public Integer getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(Integer personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Float getWorkingHoursInThisMonth() {
        return workingHoursInThisMonth;
    }

    public void setWorkingHoursInThisMonth(Float workingHoursInThisMonth) {
        this.workingHoursInThisMonth = workingHoursInThisMonth;
    }

    public DriverStatus getCurrentDriverStatus() {
        return currentDriverStatus;
    }

    public void setCurrentDriverStatus(DriverStatus currentDriverStatus) {
        this.currentDriverStatus = currentDriverStatus;
    }

    public List<WayPoint> getOrderWayPoints() {
        return orderWayPoints;
    }

    public void setOrderWayPoints(List<WayPoint> orderWayPoints) {
        this.orderWayPoints = orderWayPoints;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
