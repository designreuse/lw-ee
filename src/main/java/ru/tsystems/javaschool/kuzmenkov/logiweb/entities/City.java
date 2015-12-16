package ru.tsystems.javaschool.kuzmenkov.logiweb.entities;

import javax.persistence.*;
import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue
    @Column(name = "city_id", nullable = false, unique = true)
    private Integer cityId;

    @Column(name = "city_name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "currentCityFK")
    private List<Driver> driversInCity;

    @OneToMany(mappedBy = "currentCityFK")
    private List<Truck> trucksInCity;

    @OneToMany(mappedBy = "cityFromFK")
    private List<Freight> citiesFrom;

    @OneToMany(mappedBy = "cityToFK")
    private List<Freight> citiesTo;

    public City() {
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Driver> getDriversInCity() {
        return driversInCity;
    }

    public void setDriversInCity(List<Driver> driversInCity) {
        this.driversInCity = driversInCity;
    }

    public List<Truck> getTrucksInCity() {
        return trucksInCity;
    }

    public void setTrucksInCity(List<Truck> trucksInCity) {
        this.trucksInCity = trucksInCity;
    }

    public List<Freight> getCitiesFrom() {
        return citiesFrom;
    }

    public void setCitiesFrom(List<Freight> citiesFrom) {
        this.citiesFrom = citiesFrom;
    }

    public List<Freight> getCitiesTo() {
        return citiesTo;
    }

    public void setCitiesTo(List<Freight> citiesTo) {
        this.citiesTo = citiesTo;
    }
}
