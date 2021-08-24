package org.aleks4ay.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDto {

    private Integer number;
    private Category category;
    private int guests;
    private String description;
    private LocalDate arrival;
    private LocalDate departure;
    private String arrivalString;
    private String departureString;
    private long correctPrice;
    private String imgName;

    public OrderDto(Integer number, Category category, int guests, String description, LocalDate arrival,
                    LocalDate departure, long correctPrice, String imgName) {
        this.number = number;
        this.category = category;
        this.guests = guests;
        this.description = description;
        this.arrival = arrival;
        this.departure = departure;
        this.correctPrice = correctPrice;
        this.imgName = imgName;
    }

    private int getPeriod() {
        return (int) getArrival().until(getDeparture(), ChronoUnit.DAYS);
    }

    public double getCost() {
        return getCorrectPrice() * getPeriod();
    }
}
