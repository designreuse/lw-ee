package ru.tsystems.javaschool.kuzmenkov.logiweb.util;

import org.apache.commons.lang3.StringUtils;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Freight;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebValidationException;

/**
 * Set of static utility methods for values and fields validation.
 *
 * @author Nikolay Kuzmenkov.
 */
public class LogiwebValidator {

    private LogiwebValidator() {

    }

    /**
     * Check if driver has empty fields that should not be empty. Also check negative and incorrect values.
     * Doesn't return anything - throws exception if failed.
     *
     * @param driver
     * @throws LogiwebValidationException message that describes why validation failed.
     */
    public static void validateDriverFormValues(Driver driver) throws LogiwebValidationException {
        if (driver.getPersonalNumber() <= 0) {
            throw new LogiwebValidationException("Driver personal number can not be 0 or negative.");
        }
        else if (StringUtils.isBlank(driver.getFirstName())) {
            throw new LogiwebValidationException("Driver first name can not be empty.");
        }
        else if (!driver.getFirstName().matches("^[a-zA-Z]*$")) {
            throw new LogiwebValidationException("Driver first name has incorrect format. Use letters 'A-Z'.");
        }
        else if (StringUtils.isBlank(driver.getLastName())) {
            throw new LogiwebValidationException("Driver last name can not be empty.");
        }
        else if (!driver.getLastName().matches("^[a-zA-Z]*$")) {
            throw new LogiwebValidationException("Driver last name has incorrect format. Use letters 'A-Z'.");
        }
        else if (driver.getCurrentCityFK() == null || driver.getCurrentCityFK().getCityId() == 0) {
            throw new LogiwebValidationException("Driver current city is not set.");
        }
    }


    /**
     * Check if truck has empty fields that should not be empty. Also check negative values.
     * Doesn't return anything - throws exception if failed.
     *
     * @param truck
     * @throws LogiwebValidationException with message that describes why validation failed.
     */
    public static void validateTruckFormValues(Truck truck) throws LogiwebValidationException {
        if (truck.getTruckNumber() == null || truck.getTruckNumber().isEmpty()) {
            throw new LogiwebValidationException("Truck number is not set.");
        }
        else if (truck.getDriverCount() <= 0) {
            throw new LogiwebValidationException("Driver count can not be 0 or negative.");
        }
        else if (truck.getCapacity() <= 0) {
            throw new LogiwebValidationException("Truck capacity can not be 0 or negative.");
        }
        else if (truck.getCurrentCityFK() == null || truck.getCurrentCityFK().getCityId() == 0) {
            throw new LogiwebValidationException("Truck current city is not set.");
        }
    }

    public static void validateFreightFormValues(Freight freight) throws LogiwebValidationException {
        if (StringUtils.isBlank(freight.getDescription())) {
            throw new LogiwebValidationException("Freight description can't be blank.");
        }
        else if (freight.getWeight() <= 0d) {
            throw new LogiwebValidationException("Freight weight must be greater than 0.");
        }
        else if (freight.getCityFromFK() == null) {
            throw new LogiwebValidationException("Origin city must be specified.");
        }
        else if (freight.getCityToFK() == null) {
            throw new LogiwebValidationException("Desitnation city must be specified.");
        }
        else if (freight.getOrderForThisFreightFK() == null) {
            throw new LogiwebValidationException("Order must be specified.");
        }
    }

    /**
     * String test if it has 7 chars and contains 5 digits and 2 letters.
     *
     * @param truckNumber
     * @return boolean result.
     */
    public static boolean validateTruckNumber(String truckNumber) {
        if(!truckNumber.matches("^[A-Z0-9]{7}$")) {             // only letters and numbers (7 times)
            return false;
        }
        else if(!truckNumber.matches("^[A-Z]{2}\\d{5}$")) {
            return false;
        }
        else {
            return true;
        }
    }
}
