package ru.tsystems.javaschool.kuzmenkov.logiweb.dto;

import org.hibernate.validator.constraints.NotBlank;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.TruckStatus;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Map;

/**
 * @author Nikolay Kuzmenkov.
 */
public class TruckDTO {

    private Integer truckId;

    @NotBlank(message = "Truck number not set")
    @Pattern(regexp = "^[A-Z]{2}\\d{5}$", message = "Truck number is not valid (Example - AB67l96")
    private String truckNumber;

    @NotNull
    @Min(1)
    private Integer driverCount;

    @NotNull
    private Integer currentCityId;

    @NotNull
    @Min(value = 1, message = "Freight capacity can't be 0 or negative")
    private Float capacity;

    private Integer assignedOrderId;

    private Map<Integer, String> driversIdsAndNames;

    private TruckStatus truckStatus;

    public TruckDTO() {
        // Default constractor without parameter
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

    public Integer getCurrentCityId() {
        return currentCityId;
    }

    public void setCurrentCityId(Integer currentCityId) {
        this.currentCityId = currentCityId;
    }

    public Float getCapacity() {
        return capacity;
    }

    public void setCapacity(Float capacity) {
        this.capacity = capacity;
    }

    public Integer getAssignedOrderId() {
        return assignedOrderId;
    }

    public void setAssignedOrderId(Integer assignedOrderId) {
        this.assignedOrderId = assignedOrderId;
    }

    public Map<Integer, String> getDriversIdsAndNames() {
        return driversIdsAndNames;
    }

    public void setDriversIdsAndNames(Map<Integer, String> driversIdsAndNames) {
        this.driversIdsAndNames = driversIdsAndNames;
    }

    public TruckStatus getTruckStatus() {
        return truckStatus;
    }

    public void setTruckStatus(TruckStatus truckStatus) {
        this.truckStatus = truckStatus;
    }
}
