package ru.tsystems.javaschool.kuzmenkov.logiweb.entities;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.OrderStatus;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Nikolay on 14.11.2015.
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id", unique = true)
    private Integer orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @OneToOne(mappedBy = "orderForThisTruck")
    private Truck assignedTruckFK;

    @OneToMany(mappedBy = "orderForThisFreightFK")
    private List<Freight> orderLines;

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

    public List<Freight> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<Freight> orderLines) {
        this.orderLines = orderLines;
    }
}
