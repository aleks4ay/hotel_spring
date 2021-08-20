package org.aleks4ay.hotel.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "proposal")
public class Proposal {
    @Id
    @GeneratedValue
    private long id;

    private LocalDateTime registered = LocalDateTime.now();
    private LocalDate arrival;
    private LocalDate departure;
    private int period;
    private int guests;
    private Category category;
    private Status status;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public Proposal() {
    }

    public Proposal(LocalDate arrival, LocalDate departure, int guests, Category category) {
        this.arrival = arrival;
        this.departure = departure;
        this.guests = guests;
        this.category = category;
        setPeriod();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
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

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getPeriod() {
        if (this.period == 0) {
            return (int) getArrival().until(getDeparture(), ChronoUnit.DAYS);
        }
        return period;
    }

    public void setPeriod() {
        this.period = (int) getArrival().until(getDeparture(), ChronoUnit.DAYS);
    }

    public String getRegisteredStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        return registered.format(formatter);
    }

    @Override
    public String toString() {
        return "Proposal{" +
                "id=" + getId() +
                ", registered=" + registered +
                ", arrival=" + arrival +
                ", departure=" + departure +
                ", period=" + period +
                ", category=" + category +
                ", status=" + status +
                ", user=" + user +
                '}';
    }

    public enum Status {
        NEW,
        MANAGED,
        CONFIRMED;

        public String getTitle() {
            return this.toString();
        }
    }

    public List<Status> getStatusesValue() {
        return Arrays.asList(Status.values());
    }
}
