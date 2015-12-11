package ru.tsystems.javaschool.kuzmenkov.logiweb.entities;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.TruckStatus;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Nikolay on 13.11.2015.
 */
@Entity
@Table(name = "trucks")
public class Truck {

    @Id
    @GeneratedValue
    @Column(name = "truck_id", unique = true)
    private Integer truckId;

    @Column(name = "truck_number", unique = true)
    private String truckNumber;

    @Column(name = "driver_count")
    private Integer driverCount;

    @Column(name = "capacity")
    private Float capacity;

    @Column(name = "truck_status")
    @Enumerated(EnumType.STRING)
    private TruckStatus truckStatus;

    @OneToMany(mappedBy = "currentTruckFK")
    private List<Driver> driversInTruck;

    @ManyToOne
    @JoinColumn(name = "current_truck_location_FK")
    private City currentCityFK;

    @OneToOne
    @JoinColumn(name = "order_for_this_truck_FK")
    private Order orderForThisTruck;

    public Truck() {

    }

    public Integer getTruckId() {
        return truckId;
    }

    public void setTruckId(Integer truckId) {
        this.truckId = truckId;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public Integer getDriverCount() {
        return driverCount;
    }

    public void setDriverCount(Integer driverCount) {
        this.driverCount = driverCount;
    }

    public Float getCapacity() {
        return capacity;
    }

    public void setCapacity(Float capacity) {
        this.capacity = capacity;
    }

    public TruckStatus getTruckStatus() {
        return truckStatus;
    }

    public void setTruckStatus(TruckStatus truckStatus) {
        this.truckStatus = truckStatus;
    }

    public List<Driver> getDriversInTruck() {
        return driversInTruck;
    }

    public void setDriversInTruck(List<Driver> driversInTruck) {
        this.driversInTruck = driversInTruck;
    }

    public City getCurrentCityFK() {
        return currentCityFK;
    }

    public void setCurrentCityFK(City currentCityFK) {
        this.currentCityFK = currentCityFK;
    }

    public Order getOrderForThisTruck() {
        return orderForThisTruck;
    }

    public void setOrderForThisTruck(Order orderForThisTruck) {
        this.orderForThisTruck = orderForThisTruck;
    }
}
