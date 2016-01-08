package ru.tsystems.javaschool.kuzmenkov.logiweb.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author Nikolay Kuzmenkov.
 */
@Entity
@Table(name = "shift_records")
public class DriverShift {

    @Id
    @GeneratedValue
    @Column(name = "driver_shift_id", nullable = false, unique = true)
    private Integer driverShiftId;

    @Column(name = "driver_shift_begin", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date driverShiftBegin;

    @Column(name = "driver_shift_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date driverShiftEnd;

    @ManyToOne
    @JoinColumn(name = "driver_for_this_shift_FK", nullable = false)
    private Driver driverForThisShiftFK;

    public DriverShift() {
        // Default constructor without parameters.
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
