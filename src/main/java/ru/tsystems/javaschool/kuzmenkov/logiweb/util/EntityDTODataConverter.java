package ru.tsystems.javaschool.kuzmenkov.logiweb.util;

import org.springframework.stereotype.Component;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.DriverDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.OrderDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.dto.TruckDTO;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.City;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Driver;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Order;
import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.Truck;

import java.util.*;

/**
 * @author Nikolay Kuzmenkov.
 */
@Component
public class EntityDTODataConverter {

    /**
     * Convert driver object from DTO to entity.
     *
     * @param driverDTO driverDTO
     * @return Driver driverEntity
     */
    public Driver convertDriverDTOToEntity(DriverDTO driverDTO) {
        Driver driverEntity = new Driver();

        City city = new City();
        city.setCityId(driverDTO.getCurrentCityId());

        driverEntity.setCurrentCityFK(city);
        driverEntity.setFirstName(driverDTO.getFirstName());
        driverEntity.setLastName(driverDTO.getLastName());
        driverEntity.setPersonalNumber(driverDTO.getPersonalNumber());

        return driverEntity;
    }

    public DriverDTO convertDriverEntityToDTO(Driver driverEntity) {
        DriverDTO driverDTO = new DriverDTO();

        driverDTO.setCurrentCityId(driverEntity.getCurrentCityFK() == null ? 0 : driverEntity.
                getCurrentCityFK().getCityId());

        driverDTO.setCurrentTruckNumber(driverEntity.getCurrentTruckFK() == null ? null
                : driverEntity.getCurrentTruckFK().getTruckNumber());
        driverDTO.setPersonalNumber(driverEntity.getPersonalNumber());
        driverDTO.setDriverId(driverEntity.getDriverId());
        driverDTO.setFirstName(driverEntity.getFirstName());

        if (driverDTO.getCurrentTruckNumber() != null) {
            driverDTO.setCurrentOrderId(driverEntity.getCurrentTruckFK().getOrderForThisTruck().getOrderId());
        }

        if (driverDTO.getCurrentTruckNumber() != null
                && driverEntity.getCurrentTruckFK().getDriversInTruck() != null) {
            Set<Integer> coDriversIds = new HashSet<Integer>();
            Set<Driver> driversInTruck = driverEntity.getCurrentTruckFK().getDriversInTruck();

            for (Driver driver : driversInTruck) {
                coDriversIds.add(driver.getDriverId());
            }

            driverDTO.setCoDriversIds(coDriversIds);
        }

        driverDTO.setOrderRouteInfoForDriver(null);

        driverDTO.setDriverStatus(driverEntity.getDriverStatus());
        driverDTO.setLastName(driverEntity.getLastName());
        driverDTO.setWorkingHoursThisMonth(0f);

        return driverDTO;
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

    public TruckDTO convertTruckEntityToDTO(Truck truckEntity) {
        TruckDTO truckDTO = new TruckDTO();

        truckDTO.setCurrentCityId(truckEntity.getCurrentCityFK() == null ? 0 : truckEntity
                .getCurrentCityFK().getCityId());

        truckDTO.setTruckId(truckEntity.getTruckId());
        truckDTO.setTruckNumber(truckEntity.getTruckNumber());

        if (truckEntity.getOrderForThisTruck() != null) {
            truckDTO.setAssignedOrderId(truckEntity.getOrderForThisTruck().getOrderId());
        }

        if (truckEntity.getDriversInTruck() != null) {
            Map<Integer, String> driversIdsAndSurnames = new HashMap<>();

            for (Driver driver : truckEntity.getDriversInTruck()) {
                driversIdsAndSurnames.put(driver.getDriverId(), driver.getLastName());
            }

            truckDTO.setDriversIdsAndNames(driversIdsAndSurnames);
        }

        truckDTO.setCapacity(truckEntity.getCapacity());
        truckDTO.setDriverCount(truckEntity.getDriverCount());
        truckDTO.setTruckStatus(truckEntity.getTruckStatus());

        return truckDTO;
    }

    public Set<DriverDTO> convertListDriverEntitiesToDTO(Collection<Driver> driverEntities) {
        Set<DriverDTO> driversDTO = new HashSet<>();

        for (Driver entity : driverEntities) {
            driversDTO.add(convertDriverEntityToDTO(entity));
        }
        return driversDTO;
    }

    public List<TruckDTO> convertListTruckEntitiesToDTO(List<Truck> truckEntities) {
        List<TruckDTO> trucksDTO = new ArrayList<>();

        for (Truck entity : truckEntities) {
            trucksDTO.add(convertTruckEntityToDTO(entity));
        }
        return trucksDTO;
    }

    public OrderDTO convertOrderEntityToDTO(Order orderEntity) {
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setOrderId(orderEntity.getOrderId());
        orderDTO.setOrderStatus(orderEntity.getOrderStatus());

        if (orderEntity.getOrderLines() != null) {
            orderDTO.setFreightsOrderLines(orderEntity.getOrderLines());
        }

        if (orderEntity.getAssignedTruckFK() != null) {
            orderDTO.setAssignedTruck(convertTruckEntityToDTO(orderEntity.getAssignedTruckFK()));
        }
        return orderDTO;
    }
}
