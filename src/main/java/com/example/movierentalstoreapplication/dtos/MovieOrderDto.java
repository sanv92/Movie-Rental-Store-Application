package com.example.movierentalstoreapplication.dtos;

import java.util.List;
import java.util.Objects;

public class MovieOrderDto {
    private Long id;
    private Long customerId;
    private String status;
    private List<MovieRentalDto> rentals;

    private Double totalPrice;
    private Double totalPriceForAllDay;
    private Double totalDiscount;
    private Double totalPriceWithLateCharge;
    private Integer totalPoints;
    private Double totalLateCharge;

    public MovieOrderDto() {
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getStatus() {
        return status;
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

    public Double getTotalPriceForAllDay() {
        return totalPriceForAllDay;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public MovieOrderDto setId(Long id) {
        this.id = id;
        return this;
    }

    public MovieOrderDto setCustomerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    public MovieOrderDto setStatus(String status) {
        this.status = status;
        return this;
    }

    public MovieOrderDto setRentals(List<MovieRentalDto> rentals) {
        this.rentals = rentals;
        return this;
    }

    public MovieOrderDto setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public MovieOrderDto setTotalPriceWithLateCharge(Double totalPriceWithLateCharge) {
        this.totalPriceWithLateCharge = totalPriceWithLateCharge;
        return this;
    }

    public MovieOrderDto setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
        return this;
    }

    public MovieOrderDto setTotalLateCharge(Double totalLateCharge) {
        this.totalLateCharge = totalLateCharge;
        return this;
    }

    public MovieOrderDto setTotalPriceForAllDay(Double totalPriceForAllDay) {
        this.totalPriceForAllDay = totalPriceForAllDay;
        return this;
    }

    public MovieOrderDto setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieOrderDto)) return false;
        MovieOrderDto that = (MovieOrderDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, status);
    }

    @Override
    public String toString() {
        return "MovieOrderDto{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", status='" + status + '\'' +
                ", rentals=" + rentals +
                ", totalPrice=" + totalPrice +
                ", totalPriceWithLateCharge=" + totalPriceWithLateCharge +
                ", totalPoints=" + totalPoints +
                ", totalLateCharge=" + totalLateCharge +
                '}';
    }
}