package org.aleks4ay.hotel.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private long id;
    private LocalDateTime registered = LocalDateTime.now();
    private double correctPrice;
    private Status status;

//    private Room room;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public Order() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public Room getRoom() {
//        return room;
//    }
/*
    public void setRoom(Room room) {
        this.room = room;
        this.correctPrice = room.getPrice();
    }*/

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
        this.correctPrice = schedule.getRoom().getPrice();
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getCorrectPrice() {
        return correctPrice;
    }

    public void setCorrectPrice(Double price) {
        this.correctPrice = price;
    }

    public double getCost() {
        return getCorrectPrice() * getSchedule().getPeriod();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", registered=" + registered +
                ", arrival=" + schedule.getArrival() +
                ", departure=" + schedule.getDeparture() +
                ", correctPrice=" + correctPrice +
                ", status=" + status +
//                ", room=" + room.getNumber() +
                ", user=" + user.getLogin() +
                '}';
    }

    public enum Status {
        NEW,
        CONFIRMED,
//        MANAGED,
        PAID,
        CANCELED,
        COMPLETED
    }
}
