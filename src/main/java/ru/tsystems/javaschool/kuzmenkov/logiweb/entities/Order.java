package ru.tsystems.javaschool.kuzmenkov.logiweb.entities;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.OrderStatus;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * @author Nikolay Kuzmenkov.
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id", nullable = false, unique = true)
    private Integer orderId;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToOne(mappedBy = "orderForThisTruck")
    private Truck assignedTruckFK;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "orderForThisFreightFK")
    private Set<Freight> orderLines;

    public Order() {
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

    public Truck getAssignedTruckFK() {
        return assignedTruckFK;
    }

    public void setAssignedTruckFK(Truck assignedTruckFK) {
        this.assignedTruckFK = assignedTruckFK;
    }

    public Set<Freight> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(Set<Freight> orderLines) {
        this.orderLines = orderLines;
    }
}
