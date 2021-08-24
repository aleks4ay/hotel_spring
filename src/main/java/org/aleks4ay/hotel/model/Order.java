package org.aleks4ay.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    private LocalDate arrival;
    private LocalDate departure;
    private int period;
    private int guests;
    @Enumerated(EnumType.STRING)
    private Category category;
    private LocalDateTime registered = LocalDateTime.now();

    @Column(name = "correct_price", precision = 12, scale = 2)
    private long correctPrice;

    @Column(name = "status")
    private Status status;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "order_room",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private Room room;

    public void setRoomDeeply(Room room) {
        this.room = room;
        this.correctPrice = room.getPrice();
        room.addOrder(this);
    }


    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "order_invoice",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "invoice_id")
    )
    private Invoice invoice;


    public Order(LocalDate arrival, LocalDate departure, int guests, Category category,
                 LocalDateTime registered, long correctPrice) {
        this.arrival = arrival;
        this.departure = departure;
        this.period = (int) this.arrival.until(this.departure, ChronoUnit.DAYS);
        this.guests = guests;
        this.category = category;
        this.registered = registered;
        this.correctPrice = correctPrice;
    }


    public long getCost() {
        return getCorrectPrice() * getPeriod();
    }

    public String getRegisteredStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        return registered.format(formatter);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", arrival=" + arrival +
                ", departure=" + departure +
                ", period=" + period +
                ", guests=" + guests +
                ", category=" + category +
                ", registered=" + registered +
                ", correctPrice=" + correctPrice +
                ", status=" + status +
                ", user=" + user.getLogin() +
                '}';
    }

    public enum Status {
        NEW,
        BOOKED,
        CONFIRMED,
        PAID,
        CANCEL
    }
}