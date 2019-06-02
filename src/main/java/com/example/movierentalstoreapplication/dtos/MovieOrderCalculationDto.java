package com.example.movierentalstoreapplication.dtos;

import java.util.List;
import java.util.Objects;

public class MovieOrderCalculationDto {
    private Long id;
    private List<MovieRentalDto> rentals;
    private Double totalPrice;
    private Double totalPriceForAllDays;
    private Double totalDiscount;
    private Double totalPriceWithLateCharge;
    private Integer totalPoints;
    private Double totalLateCharge;

    public MovieOrderCalculationDto() {
    }

    public List<MovieRentalDto> getRentals() {
        return rentals;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public Double getTotalPriceWithLateCharge() {
        return totalPriceWithLateCharge;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public Double getTotalLateCharge() {
        return totalLateCharge;
    }

    public Double getTotalPriceForAllDays() {
        return totalPriceForAllDays;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public MovieOrderCalculationDto setId(Long id) {
        this.id = id;
        return this;
    }

    public MovieOrderCalculationDto setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public MovieOrderCalculationDto setTotalPriceWithLateCharge(Double totalPriceWithLateCharge) {
        this.totalPriceWithLateCharge = totalPriceWithLateCharge;
        return this;
    }

    public MovieOrderCalculationDto setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
        return this;
    }

    public MovieOrderCalculationDto setTotalLateCharge(Double totalLateCharge) {
        this.totalLateCharge = totalLateCharge;
        return this;
    }

    public MovieOrderCalculationDto setTotalPriceForAllDays(Double totalPriceForAllDays) {
        this.totalPriceForAllDays = totalPriceForAllDays;
        return this;
    }

    public MovieOrderCalculationDto setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
        return this;
    }

    public MovieOrderCalculationDto setRentals(List<MovieRentalDto> rentals) {
        this.rentals = rentals;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieOrderCalculationDto)) return false;
        MovieOrderCalculationDto that = (MovieOrderCalculationDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(totalPrice, that.totalPrice) &&
                Objects.equals(totalPriceForAllDays, that.totalPriceForAllDays) &&
                Objects.equals(totalDiscount, that.totalDiscount) &&
                Objects.equals(totalPoints, that.totalPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalPrice, totalPriceForAllDays, totalDiscount, totalPriceWithLateCharge, totalPoints);
    }

    @Override
    public String toString() {
        return "MovieOrderCalculationDto{" +
                "id=" + id +
                ", totalPrice=" + totalPrice +
                ", totalPriceForAllDay=" + totalPriceForAllDays +
                ", totalDiscount=" + totalDiscount +
                ", totalPriceWithLateCharge=" + totalPriceWithLateCharge +
                ", totalPoints=" + totalPoints +
                ", totalLateCharge=" + totalLateCharge +
                '}';
    }
}