package org.aleks4ay.hotel.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Data
@ToString
@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue
    private long id;

    private LocalDateTime registered = LocalDateTime.now();

    @Column(name = "cost", precision = 12, scale = 2)
    private long cost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Invoice.Status status;

    @OneToOne(mappedBy = "invoice", fetch = FetchType.EAGER)
    private Order order;


    public Invoice(LocalDateTime registered, Status status, Order order) {
        this.registered = registered;
        this.cost = order.getCost();
        this.status = status;
        this.order = order;
        order.setInvoice(this);
    }

    public String getRegisteredStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        return registered.format(formatter);
    }
    public String getEndDateStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        return registered.plusDays(2).format(formatter);
    }

    public enum Status {
        NEW,
        PAID,
        CANCEL
    }
}
