package ru.tsystems.javaschool.kuzmenkov.logiweb.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Nikolay Kuzmenkov.
 */
public class DriverDTO {

    private Integer driverId;
    @NotNull(message = "hello")
    @Min(value = 3, message = "hello2")

    /*@Pattern(regexp = "^[1-9]*$", message = "Personal number field is in wrong format. Use integers.")*/
    private Integer personalNumber;
    @NotNull
    @Size(min = 1, max = 256, message = "Name should be 1-256 chars long.")
    private String firstName;

    private String lastName;

    @NotNull
    private Integer currentCityFK;

    private Integer orderId;

    public DriverDTO() {

    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public Integer getCurrentCityFK() {
        return currentCityFK;
    }

    public void setCurrentCityFK(Integer currentCityFK) {
        this.currentCityFK = currentCityFK;
    }
}
