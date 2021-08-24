package org.aleks4ay.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Table(name = "room")
public class Room implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    @Column(name = "number", unique = true)
    private int number;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;
    private int guests;
    private String description;
    @Column(name = "price", precision = 12, scale = 2)
    private long price;
    private String imgName;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();

    public Room(int number, Category category, int guests, String description, long price, String imgName) {
        this.number = number;
        this.category = category;
        this.guests = guests;
        this.description = description;
        this.price = price;
        this.imgName = imgName;
    }

    void addOrder(Order order) {
        orders.add(order);
    }
}
