package ru.tsystems.javaschool.kuzmenkov.logiweb.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Nikolay on 14.11.2015.
 */
@Entity
@Table(name = "shift_records")
public class DriverShift {

    @Id
    @GeneratedValue
    @Column(name = "driver_shift_id", unique = true)
    private Integer driverShiftId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "driver_shift_begin")
    private Date driverShiftBegin;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "driver_shift_end")
    private Date driverShiftEnd;

    @ManyToOne
    @JoinColumn(name = "driver_for_this_shift_FK")
    private Driver driverForThisShiftFK;

    public DriverShift() {

    }

    public Integer getDriverShiftId() {
        return driverShiftId;
    }

    public void setDriverShiftId(Integer driverShiftId) {
        this.driverShiftId = driverShiftId;
    }

    public Date getDriverShiftBegin() {
        return driverShiftBegin;
    }

    public void setDriverShiftBegin(Date driverShiftBegin) {
        this.driverShiftBegin = driverShiftBegin;
    }

    public Date getDriverShiftEnd() {
        return driverShiftEnd;
    }

    public void setDriverShiftEnd(Date driverShiftEnd) {
        this.driverShiftEnd = driverShiftEnd;
    }

    public Driver getDriverForThisShiftFK() {
        return driverForThisShiftFK;
    }

    public void setDriverForThisShiftFK(Driver driverForThisShiftFK) {
        this.driverForThisShiftFK = driverForThisShiftFK;
    }
}
