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
}
