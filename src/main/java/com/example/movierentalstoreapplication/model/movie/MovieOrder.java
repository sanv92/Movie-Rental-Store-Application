package com.example.movierentalstoreapplication.model.movie;

import com.example.movierentalstoreapplication.model.Customer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class MovieOrder {

    public enum Status {
        OPENED,
        CLOSED,
        UNKNOWN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_generator")
    @SequenceGenerator(
            name = "order_id_generator",
            sequenceName = "order_id_seq",
            allocationSize = 1
    )
    private Long id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "customer_id", updatable = false, nullable = false)
    private Customer customer;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.UNKNOWN;

    @Size(min=1)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", fetch = FetchType.EAGER)
    private List<MovieRental> rentals;

    public MovieOrder() {
        this.rentals = new ArrayList<>();
    }

    public MovieOrder(Customer customer, Status status) {
        this.customer = customer;
        this.status = status;
        this.rentals = new ArrayList<>();
    }

    public MovieOrder(Customer customer, Status status, List<MovieRental> rentals) {
        this.customer = customer;
        this.status = status;
        this.rentals = rentals;
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Status getStatus() {
        return status;
    }

    public List<MovieRental> getRentals() {
        return rentals;
    }

    public boolean isOpened() {
        return status == Status.OPENED;
    }

    public boolean isClosed() {
        return status == Status.CLOSED;
    }

    public MovieOrder setStatus(Status status) {
        this.status = status;
        return this;
    }

    public MovieOrder setRentals(List<MovieRental> rentals) {
        this.rentals = rentals;
        return this;
    }

    public double calculateTotalPrice() {
        return rentals.stream()
                .map(MovieRental::getPrice)
                .reduce(0d, Double::sum);
    }

    public double calculateTotalPriceByDays() {
        return rentals.stream()
                .map(MovieRental::calculatePriceByDays)
                .reduce(0d, Double::sum);
    }

    public double calculateTotalDiscount() {
        return rentals.stream()
                .map(MovieRental::calculateDiscount)
                .reduce(0d, Double::sum);
    }

    public double calculateTotalPriceByDaysWithDiscount() {
        return rentals.stream()
                .map(MovieRental::calculatePriceByDaysWithDiscount)
                .reduce(0d, Double::sum);
    }

    public double calculateTotalPriceWithLateCharge() {
        return this.calculateTotalPriceByDaysWithDiscount() + this.calculateTotalLateCharge();
    }

    public double calculateTotalLateCharge() {
        return rentals.stream()
                .map(MovieRental::getLateCharge)
                .reduce(0d, Double::sum);
    }

    public int calculateTotalPoints() {
        return rentals.stream()
                .map(MovieRental::getPoints)
                .reduce(0, Integer::sum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieOrder)) return false;
        MovieOrder that = (MovieOrder) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(customer, that.customer) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, status);
    }

    @Override
    public String toString() {
        return "MovieOrder{" +
                "id=" + id +
                ", customer=" + customer +
                ", status=" + status +
                ", rentals=" + rentals +
                '}';
    }
}