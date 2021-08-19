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
    private double correctPrice;

    public int getPeriod() {
        return (int) getArrival().until(getDeparture(), ChronoUnit.DAYS);
    }

    public double getCost() {
        return getCorrectPrice() * getPeriod();
    }
}
