package ru.tsystems.javaschool.kuzmenkov.logiweb.entities;

import javax.persistence.*;

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


    public City() {
        // Default constructor without parameters.
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
}
