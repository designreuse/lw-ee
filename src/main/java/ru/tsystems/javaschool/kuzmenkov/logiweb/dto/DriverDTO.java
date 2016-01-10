package ru.tsystems.javaschool.kuzmenkov.logiweb.dto;

import org.hibernate.validator.constraints.NotBlank;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.DriverShift;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.OrderRoute;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.DriverStatus;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

/**
 * @author Nikolay Kuzmenkov.
 */
public class DriverDTO {

    private Integer driverId;
    @NotNull(message = "Driver personal number can not be empty")
    private Integer personalNumber;
    @NotBlank(message = "Driver first name can not be empty")
    @Size(min = 1, max = 256, message = "Name should be 1-256 chars long.")
    private String firstName;
    @NotBlank(message = "Driver lastst name can not be empty")
    private String lastName;

    private DriverStatus driverStatus;

    @NotNull
    private Integer currentCityId;

    private Integer currentOrderId;

    private Float workingHoursThisMonth;

    private String currentTruckNumber;

    private Set<Integer> coDriversIds;

    private OrderRoute orderRouteInfoForDriver;

    private List<DriverShift> driverShiftRecordsForThisMonth;

    public DriverDTO() {
        // Default constructor without parameter.
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

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

    public DriverStatus getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
    }

    public Integer getCurrentCityId() {
        return currentCityId;
    }

    public void setCurrentCityId(Integer currentCityId) {
        this.currentCityId = currentCityId;
    }

    public Integer getCurrentOrderId() {
        return currentOrderId;
    }

    public void setCurrentOrderId(Integer currentOrderId) {
        this.currentOrderId = currentOrderId;
    }

    public Float getWorkingHoursThisMonth() {
        return workingHoursThisMonth;
    }

    public void setWorkingHoursThisMonth(Float workingHoursThisMonth) {
        this.workingHoursThisMonth = workingHoursThisMonth;
    }

    public String getCurrentTruckNumber() {
        return currentTruckNumber;
    }

    public void setCurrentTruckNumber(String currentTruckNumber) {
        this.currentTruckNumber = currentTruckNumber;
    }

    public Set<Integer> getCoDriversIds() {
        return coDriversIds;
    }

    public void setCoDriversIds(Set<Integer> coDriversIds) {
        this.coDriversIds = coDriversIds;
    }

    public OrderRoute getOrderRouteInfoForDriver() {
        return orderRouteInfoForDriver;
    }

    public void setOrderRouteInfoForDriver(OrderRoute orderRouteInfoForDriver) {
        this.orderRouteInfoForDriver = orderRouteInfoForDriver;
    }

    public List<DriverShift> getDriverShiftRecordsForThisMonth() {
        return driverShiftRecordsForThisMonth;
    }

    public void setDriverShiftRecordsForThisMonth(List<DriverShift> driverShiftRecordsForThisMonth) {
        this.driverShiftRecordsForThisMonth = driverShiftRecordsForThisMonth;
    }
}
