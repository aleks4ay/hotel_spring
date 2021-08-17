package org.aleks4ay.hotel.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "timetable")
public class Schedule implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    private LocalDate arrival;
    private LocalDate departure;
    private RoomStatus status;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;

    public Schedule() {
    }

    public Schedule(LocalDate arrival, LocalDate departure, RoomStatus status, Room room) {
        this.arrival = arrival;
        this.departure = departure;
        this.status = status;
        this.room = room;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getArrival() {
        return arrival;
    }

    public void setArrival(LocalDate arrival) {
        this.arrival = arrival;
    }

    public LocalDate getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDate departure) {
        this.departure = departure;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int getPeriod() {
        return (int) getArrival().until(getDeparture(), ChronoUnit.DAYS);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", arrival=" + arrival +
                ", departure=" + departure +
                ", status=" + status +
                ", room=" + room +
                '}';
    }

    public enum RoomStatus {
        EMPTY,
        RESERVED,
        BOOKED,
        OCCUPIED,
        DISABLED;
    }
}
