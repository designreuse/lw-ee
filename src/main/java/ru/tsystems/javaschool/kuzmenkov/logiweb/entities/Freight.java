package ru.tsystems.javaschool.kuzmenkov.logiweb.entities;

import ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.FreightStatus;

import javax.persistence.*;

/**
 * @author Nikolay Kuzmenkov.
 */
@Entity
@Table(name = "freights")
public class Freight {

    @Id
    @GeneratedValue
    @Column(name = "freight_id", nullable = false, unique = true)
    private Integer freightId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "weight", nullable = false)
    private Float weight;

    @Column(name = "freight_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private FreightStatus freightStatus;

    @ManyToOne
    @JoinColumn(name = "city_from", nullable = false)
    private City cityFromFK;

    @ManyToOne
    @JoinColumn(name = "city_to", nullable = false)
    private City cityToFK;

    @ManyToOne
    @JoinColumn(name = "order_FK", nullable = false)
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
