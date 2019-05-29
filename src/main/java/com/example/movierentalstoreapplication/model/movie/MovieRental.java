package com.example.movierentalstoreapplication.model.movie;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Table(name = "rentals")
public class MovieRental {

    public enum Status {
        ONGOING,
        RETURNED,
        UNKNOWN,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rental_id_generator")
    @SequenceGenerator(
            name = "rental_id_generator",
            sequenceName = "rental_id_seq",
            allocationSize = 1
    )
    private Long id;

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "order_id", updatable = false, nullable = false)
    private MovieOrder order;

    @NotNull
    @OneToOne
    @JoinColumn(name = "movie_id", updatable = false, nullable = false)
    private Movie movie;

    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.UNKNOWN;
    @NotNull
    @Column(name = "price_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private PriceType priceType;

    @Min(0)
    @Column(name = "price", updatable = false, nullable = false)
    private double price = 0;
    @Min(0)
    @Column(name = "late_charge", nullable = false)
    private double lateCharge = 0;
    @Min(0)
    @Column(name = "points", updatable = false, nullable = false)
    private int points = 0;

    @Min(1)
    @Column(name = "days", updatable = false, nullable = false)
    private int numberOfDays;

    @PastOrPresent
    @Column(name = "pickup_date", updatable = false, nullable = false)
    private LocalDateTime pickupDate = LocalDateTime.now();
    @PastOrPresent
    @Column(name = "return_date")
    private LocalDateTime returnDate;

    public MovieRental() {
    }

    public MovieRental(MovieOrder movieOrder, Movie movie, int numberOfDays, int points) {
        this.order = movieOrder;
        this.movie = movie;
        this.numberOfDays = numberOfDays;
        this.points = points;
    }

    public MovieRental(MovieOrder movieOrder, Movie movie, int numberOfDays, int points, Status status) {
        this.order = movieOrder;
        this.movie = movie;
        this.numberOfDays = numberOfDays;
        this.points = points;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public MovieOrder getOrder() {
        return order;
    }

    public Movie getMovie() {
        return movie;
    }

    public Status getStatus() {
        return status;
    }

    public PriceType getPriceType() {
        return priceType;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public double getPrice() {
        return price;
    }

    public int getPoints() {
        return points;
    }

    public double getLateCharge() {
        return lateCharge;
    }

    public LocalDateTime getPickupDate() {
        return pickupDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return status == Status.RETURNED;
    }

    public boolean isOngoing() {
        return status == Status.ONGOING;
    }

    public double calculatePrice() {
        return movie.getType().calculatePrice();
    }

    public double calculatePriceByDays() {
        return movie.getType().calculatePrice(numberOfDays);
    }

    public double calculateDiscount() {
        return this.calculatePriceByDays() - this.calculatePriceByDaysWithDiscount();
    }

    public double calculatePriceByDaysWithDiscount() {
        return movie.getType().calculatePrice(numberOfDays, points);
    }

    public int calculateExtraDays() {
        int days = isReturned() ?
                (int) ChronoUnit.DAYS.between(pickupDate, returnDate) :
                (int) ChronoUnit.DAYS.between(pickupDate, LocalDateTime.now());

        return Math.max(0, days - numberOfDays);
    }

    public LocalDateTime calculateReturnDate() {
        return pickupDate.plusDays(numberOfDays);
    }

    public double calculateLateCharge() {
        if (this.isReturned()) {
            return this.getLateCharge();
        }

        LocalDateTime dateTime = LocalDateTime.now();

        int days = (int) ChronoUnit.DAYS.between(this.getPickupDate(), dateTime);
        double latePrice = this.getMovie().getType().calculatePrice(days);
        double lateChargePrice = latePrice - this.getPrice();

        return Math.max(0, lateChargePrice);
    }

    public MovieRental setStatus(Status status) {
        this.status = status;
        return this;
    }

    public MovieRental setPriceType(PriceType priceType) {
        this.priceType = priceType;
        return this;
    }

    public MovieRental setPrice(double price) {
        this.price = price;
        return this;
    }

    public MovieRental setLateCharge(double lateCharge) {
        this.lateCharge = lateCharge;
        return this;
    }

    public MovieRental setPoints(int points) {
        this.points = points;
        return this;
    }

    public MovieRental setPickupDate(LocalDateTime pickupDate) {
        this.pickupDate = pickupDate;
        return this;
    }

    public MovieRental setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
        return this;
    }

    public MovieRental setOrder(MovieOrder order) {
        this.order = order;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieRental)) return false;
        MovieRental that = (MovieRental) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(order, that.order) &&
                Objects.equals(movie, that.movie) &&
                points == that.points &&
                Double.compare(that.price, price) == 0 &&
                Objects.equals(pickupDate, that.pickupDate) &&
                Objects.equals(returnDate, that.returnDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, movie, points, price, pickupDate, returnDate);
    }

    @Override
    public String toString() {
        return "MovieRental{" +
                "id=" + id +
                ", movie=" + movie +
                ", status=" + status +
                ", priceType=" + priceType +
                ", price=" + price +
                ", lateCharge=" + lateCharge +
                ", points=" + points +
                ", numberOfDays=" + numberOfDays +
                ", pickupDate=" + pickupDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
