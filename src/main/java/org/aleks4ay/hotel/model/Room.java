package org.aleks4ay.hotel.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Room {

    private long id;
    private int number;
    private Category category;
    private int guests;
    private String description;
    private double price;
    private byte[] roomView;
    private Map<LocalDate, RoomStatus> statuses = new HashMap<>();

    public Room() {
    }

/*    public Room(int number, Category category, int guests, String description, double price) {
        this.number = number;
        this.category = category;
        this.guests = guests;
        this.description = description;
        this.price = price;
    }*/

    public Room(long id, int number, Category category, int guests, String description, double price) {
        this.id = id;
        this.number = number;
        this.category = category;
        this.guests = guests;
        this.description = description;
        this.price = price;
    }

    public boolean isEmpty(LocalDate start, LocalDate end) {
        LocalDate date = start;

        while (!date.isAfter((end))) {
            if (statuses.keySet().contains(date)) {
                return false;
            }
            date = date.plusDays(1);
        }
        return true;
    }

    public boolean isEmpty(LocalDate date) {
        if (statuses.keySet().contains(date)) {
            return false;
        }
        return true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Map<LocalDate, RoomStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(Map<LocalDate, RoomStatus> statuses) {
        this.statuses = statuses;
    }

    public boolean setStatus(LocalDate date, RoomStatus status) {
        RoomStatus oldStatus = statuses.get(date);
        if (oldStatus == null) {
            if (status.equals(RoomStatus.EMPTY)) {
                return false;
            } else {
                statuses.put(date, status);
                return true;
            }
        }
        if (status.equals(RoomStatus.EMPTY)) {
            statuses.remove(date);
            return true;
        } else {
            statuses.put(date, status);
            return true;
        }
    }



    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public byte[] getRoomView() {
        return roomView;
    }

    public void setRoomView(byte[] roomView) {
        this.roomView = roomView;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", number=" + number +
                ", category=" + category +
                ", guests=" + guests +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", statuses=" + statuses +
                '}';
    }
}
