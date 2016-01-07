package ru.tsystems.javaschool.kuzmenkov.logiweb.util;

import org.springframework.stereotype.Component;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.DriverDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.TruckDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;

/**
 * @author Nikolay Kuzmenkov.
 */
@Component
public class EntityDTODataConverter {

    public Driver convertDriverDTOToEntity(DriverDTO driverDTO) {
        Driver driverEntity = new Driver();

        City city = new City();
        city.setCityId(driverDTO.getCurrentCityFK());

        driverEntity.setCurrentCityFK(city);
        driverEntity.setFirstName(driverDTO.getFirstName());
        driverEntity.setLastName(driverDTO.getLastName());
        driverEntity.setPersonalNumber(driverDTO.getPersonalNumber());

        return driverEntity;
    }

    public Truck convertTruckDTOToEntity(TruckDTO truckDTO) {
        Truck truckEntity = new Truck();

        City city = new City();
        city.setCityId(truckDTO.getCurrentCityId());

        truckEntity.setCurrentCityFK(city);
        truckEntity.setTruckNumber(truckDTO.getTruckNumber());
        truckEntity.setDriverCount(truckDTO.getDriverCount());
        truckEntity.setCapacity(truckDTO.getCapacity());

        return truckEntity;
    }
}
