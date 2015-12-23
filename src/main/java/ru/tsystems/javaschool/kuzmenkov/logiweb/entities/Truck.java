package ru.tsystems.javaschool.kuzmenkov.logiweb.entities;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.TruckStatus;

import javax.persistence.*;
import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
@Entity
@Table(name = "trucks")
public class Truck {

    @Id
    @GeneratedValue
    @Column(name = "truck_id", nullable = false, unique = true)
    private Integer truckId;

    @Column(name = "truck_number", nullable = false, unique = true)
    private String truckNumber;

    @Column(name = "driver_count", nullable = false)
    private Integer driverCount;

    @Column(name = "capacity", nullable = false)
    private Float capacity;

    @Column(name = "truck_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TruckStatus truckStatus;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "currentTruckFK")
    private List<Driver> driversInTruck;

    @ManyToOne
    @JoinColumn(name = "current_truck_location_FK", nullable = false)
    private City currentCityFK;

    @Transient
    private Integer currentCityId;

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

    public Integer getCurrentCityId() {
        return currentCityId;
    }

    public void setCurrentCityId(Integer currentCityId) {
        this.currentCityId = currentCityId;
    }
}
