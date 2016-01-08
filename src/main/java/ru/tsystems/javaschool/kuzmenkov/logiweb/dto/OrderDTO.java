package ru.tsystems.javaschool.kuzmenkov.logiweb.dto;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Freight;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.OrderStatus;

import java.util.List;
import java.util.Set;

/**
 * @author Nikolay Kuzmenkov.
 */
public class OrderDTO {

    private Integer orderId;

    private OrderStatus orderStatus;

    private TruckDTO assignedTruck;

    private Set<Freight> freightsOrderLines;

    public OrderDTO() {
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public TruckDTO getAssignedTruck() {
        return assignedTruck;
    }

    public void setAssignedTruck(TruckDTO assignedTruck) {
        this.assignedTruck = assignedTruck;
    }

    public Set<Freight> getFreightsOrderLines() {
        return freightsOrderLines;
    }

    public void setFreightsOrderLines(Set<Freight> freightsOrderLines) {
        this.freightsOrderLines = freightsOrderLines;
    }
}
