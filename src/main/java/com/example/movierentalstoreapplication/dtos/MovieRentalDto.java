package com.example.movierentalstoreapplication.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Objects;

public class MovieRentalDto {

    private Long id;
    private MovieDto movie;
    private String status;

    private String priceType;
    private Double price;
    private Double priceForOneDay;
    private Double priceForAllDays;
    private Double discount;
    private Double lateCharge;

    private Integer points;

    private Integer numberOfDays;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime pickupDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime returnDate;
    private Integer extraDays;

    public MovieRentalDto() {
    }

    public Long getId() {
        return id;
    }

    public MovieDto getMovie() {
        return movie;
    }

    public String getStatus() {
        return status;
    }

    public String getPriceType() {
        return priceType;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getPoints() {
        return points;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public LocalDateTime getPickupDate() {
        return pickupDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public Integer getExtraDays() {
        return extraDays;
    }

    public Double getLateCharge() {
        return lateCharge;
    }

    public Double getPriceForOneDay() {
        return priceForOneDay;
    }

    public Double getPriceForAllDays() {
        return priceForAllDays;
    }

    public Double getDiscount() {
        return discount;
    }

    public MovieRentalDto setId(Long id) {
        this.id = id;
        return this;
    }

    public MovieRentalDto setMovie(MovieDto movie) {
        this.movie = movie;
        return this;
    }

    public MovieRentalDto setStatus(String status) {
        this.status = status;
        return this;
    }

    public MovieRentalDto setPriceType(String priceType) {
        this.priceType = priceType;
        return this;
    }

    public MovieRentalDto setPrice(Double price) {
        this.price = price;
        return this;
    }

    public MovieRentalDto setPoints(Integer points) {
        this.points = points;
        return this;
    }

    public MovieRentalDto setNumberOfDays(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
        return this;
    }

    public MovieRentalDto setPickupDate(LocalDateTime pickupDate) {
        this.pickupDate = pickupDate;
        return this;
    }

    public MovieRentalDto setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
        return this;
    }

    public MovieRentalDto setExtraDays(Integer extraDays) {
        this.extraDays = extraDays;
        return this;
    }

    public MovieRentalDto setLateCharge(Double lateCharge) {
        this.lateCharge = lateCharge;
        return this;
    }

    public MovieRentalDto setPriceForOneDay(Double priceForOneDay) {
        this.priceForOneDay = priceForOneDay;
        return this;
    }

    public MovieRentalDto setPriceForAllDays(Double priceForAllDays) {
        this.priceForAllDays = priceForAllDays;
        return this;
    }

    public MovieRentalDto setDiscount(Double discount) {
        this.discount = discount;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieRentalDto)) return false;
        MovieRentalDto that = (MovieRentalDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(movie, that.movie) &&
                Objects.equals(status, that.status) &&
                Objects.equals(price, that.price) &&
                Objects.equals(points, that.points) &&
                Objects.equals(pickupDate, that.pickupDate) &&
                Objects.equals(returnDate, that.returnDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie, status, price, points, pickupDate, returnDate);
    }

    @Override
    public String toString() {
        return "MovieRentalDto{" +
                "id=" + id +
                ", movie=" + movie +
                ", status='" + status + '\'' +
                ", priceType='" + priceType + '\'' +
                ", price=" + price +
                ", points=" + points +
                ", numberOfDays=" + numberOfDays +
                ", pickupDate=" + pickupDate +
                ", returnDate=" + returnDate +
                ", extraDays=" + extraDays +
                ", lateCharge=" + lateCharge +
                '}';
    }
}