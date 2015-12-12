package ru.tsystems.javaschool.kuzmenkov.logiweb.entities;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.DriverStatus;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Nikolay on 13.11.2015.
 */
@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue
    @Column(name = "driver_id", unique = true)
    private Integer driverId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "personal_number", unique = true)
    private Integer personalNumber;

    @Column(name = "driver_status")
    @Enumerated(EnumType.STRING)
    private DriverStatus driverStatus;

    @Column(name = "working_hours_for_this_mounth")
    private Integer workingHoursThisMonth;

    @OneToMany(mappedBy = "driverForThisShiftFK")
    private List<DriverShift> driverShiftRecords;

    @ManyToOne
    @JoinColumn(name = "current_driver_location_FK")
    private City currentCityFK;

    @ManyToOne
    @JoinColumn(name = "current_truck_FK")
    private Truck currentTruckFK;

    @OneToOne
    @JoinColumn(name = "driver_logiweb_account_id")
    private User logiwebDriverAccount;

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

    public Integer getWorkingHoursThisMonth() {
        return workingHoursThisMonth;
    }

    public void setWorkingHoursThisMonth(Integer workingHoursThisMonth) {
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
}
