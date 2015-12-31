package ru.tsystems.javaschool.kuzmenkov.logiweb.entities;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.DriverStatus;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue
    @Column(name = "driver_id", nullable = false, unique = true)
    private Integer driverId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "personal_number", nullable = false, unique = true)
    private Integer personalNumber;

    @Column(name = "driver_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DriverStatus driverStatus;

    @OneToMany(fetch = FetchType.LAZY ,mappedBy = "driverForThisShiftFK")
    private List<DriverShift> driverShiftRecords;

    @ManyToOne
    @JoinColumn(name = "current_driver_location_FK", nullable = false)
    private City currentCityFK;

    @ManyToOne
    @JoinColumn(name = "current_truck_FK")
    private Truck currentTruckFK;

    @OneToOne
    @JoinColumn(name = "driver_logiweb_account_id")
    private User logiwebDriverAccount;

    @Transient
    private OrderRoute orderRouteInfoForThisDriver;
    @Transient
    private Float workingHoursThisMonth;

    public Driver() {
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
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

    public Integer getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(Integer personalNumber) {
        this.personalNumber = personalNumber;
    }

    public DriverStatus getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
    }

    public Float getWorkingHoursThisMonth() {
        return workingHoursThisMonth;
    }

    public void setWorkingHoursThisMonth(Float workingHoursThisMonth) {
        this.workingHoursThisMonth = workingHoursThisMonth;
    }

    public List<DriverShift> getDriverShiftRecords() {
        return driverShiftRecords;
    }

    public void setDriverShiftRecords(List<DriverShift> driverShiftRecords) {
        this.driverShiftRecords = driverShiftRecords;
    }

    public City getCurrentCityFK() {
        return currentCityFK;
    }

    public void setCurrentCityFK(City currentCityFK) {
        this.currentCityFK = currentCityFK;
    }

    public Truck getCurrentTruckFK() {
        return currentTruckFK;
    }

    public void setCurrentTruckFK(Truck currentTruckFK) {
        this.currentTruckFK = currentTruckFK;
    }

    public User getLogiwebDriverAccount() {
        return logiwebDriverAccount;
    }

    public void setLogiwebDriverAccount(User logiwebDriverAccount) {
        this.logiwebDriverAccount = logiwebDriverAccount;
    }

    public OrderRoute getOrderRouteInfoForThisDriver() {
        return orderRouteInfoForThisDriver;
    }

    public void setOrderRouteInfoForThisDriver(OrderRoute orderRouteInfoForThisDriver) {
        this.orderRouteInfoForThisDriver = orderRouteInfoForThisDriver;
    }
}
