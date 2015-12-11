package ru.tsystems.javaschool.kuzmenkov.logiweb.entities;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.FreightStatus;

import javax.persistence.*;

/**
 * Created by Nikolay on 14.11.2015.
 */
@Entity
@Table(name = "freights")
public class Freight {

    @Id
    @GeneratedValue
    @Column(name = "freight_id", unique = true)
    private Integer freightId;

    @Column(name = "description")
    private String description;

    @Column(name = "weight")
    private Float weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "freight_status")
    private FreightStatus freightStatus;

    @ManyToOne
    @JoinColumn(name = "city_from")
    private City cityFromFK;

    @ManyToOne
    @JoinColumn(name = "city_to")
    private City cityToFK;

    @ManyToOne
    @JoinColumn(name = "order_FK")
    private Order orderForThisFreightFK;

    public Freight() {

    }

    public Integer getFreightId() {
        return freightId;
    }

    public void setFreightId(Integer freightId) {
        this.freightId = freightId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public FreightStatus getFreightStatus() {
        return freightStatus;
    }

    public void setFreightStatus(FreightStatus freightStatus) {
        this.freightStatus = freightStatus;
    }

    public City getCityFromFK() {
        return cityFromFK;
    }

    public void setCityFromFK(City cityFromFK) {
        this.cityFromFK = cityFromFK;
    }

    public City getCityToFK() {
        return cityToFK;
    }

    public void setCityToFK(City cityToFK) {
        this.cityToFK = cityToFK;
    }

    public Order getOrderForThisFreightFK() {
        return orderForThisFreightFK;
    }

    public void setOrderForThisFreightFK(Order orderForThisFreightFK) {
        this.orderForThisFreightFK = orderForThisFreightFK;
    }
}
