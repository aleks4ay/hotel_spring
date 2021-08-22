package org.aleks4ay.hotel.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private double price;
    private String imgName;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
    private List<Schedule> schedules;

    public Room() {
    }

    public Room(int number, Category category, int guests, String description, double price, String imgName) {
        this.number = number;
        this.category = category;
        this.guests = guests;
        this.description = description;
        this.price = price;
        this.imgName = imgName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public boolean isEmpty(LocalDate start, LocalDate end) {
        for (Schedule t : schedules) {
            if (!start.isBefore(t.getArrival()) && !start.isAfter(t.getDeparture())
                    || !end.isBefore(t.getArrival()) && !end.isAfter(t.getDeparture()) ) {
                System.out.println("found occupied room: " + start + " - " + end);
                System.out.println("Schedule: " + t.getArrival() + " - " + t.getDeparture());
                return false;
            }
        }
        return true;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        for ( Schedule sch : schedules) {
            sch.setRoom(this);
            this.schedules.add(sch);
        }
    }

    public void addSchedule(Schedule schedule) {
        schedule.setRoom(this);
        if (this.schedules == null) {
            this.schedules = new ArrayList<>();
        }
        this.schedules.add(schedule);
    }


    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", number=" + number +
                ", category=" + category +
                ", guests=" + guests +
                ", description='" + description + '\'' +
                ", imgName=" + imgName +
                ", price=" + price +
                '}';
    }

}
