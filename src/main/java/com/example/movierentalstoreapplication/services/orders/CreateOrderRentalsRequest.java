package com.example.movierentalstoreapplication.services.orders;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

public class CreateOrderRentalsRequest {
    @NotNull
    @Min(1)
    private Long customerId;

    @Valid
    @Size(min = 1)
    private List<Rental> rentals;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }

    public CreateOrderRentalsRequest(@NotNull @Min(1) Long customerId, @Valid @Size(min = 1) List<Rental> rentals) {
        this.customerId = customerId;
        this.rentals = rentals;
    }

    public static class Rental {
        @NotNull
        @Min(1)
        private Long movieId;

        @NotNull
        @Min(1)
        private Integer numberOfDays;

        @NotNull
        @Min(0)
        private Integer bonusPoints;

        public Rental(@NotNull @Min(1) Long movieId, @NotNull @Min(1) Integer numberOfDays, @NotNull @Min(0) Integer bonusPoints) {
            this.movieId = movieId;
            this.numberOfDays = numberOfDays;
            this.bonusPoints = bonusPoints;
        }

        public Long getMovieId() {
            return movieId;
        }

        public void setMovieId(Long movieId) {
            this.movieId = movieId;
        }

        public Integer getNumberOfDays() {
            return numberOfDays;
        }

        public void setNumberOfDays(Integer numberOfDays) {
            this.numberOfDays = numberOfDays;
        }

        public Integer getBonusPoints() {
            return bonusPoints;
        }

        public void setBonusPoints(Integer bonusPoints) {
            this.bonusPoints = bonusPoints;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Rental)) return false;
            Rental rental = (Rental) o;
            return Objects.equals(movieId, rental.movieId) &&
                    Objects.equals(numberOfDays, rental.numberOfDays) &&
                    Objects.equals(bonusPoints, rental.bonusPoints);
        }

        @Override
        public int hashCode() {
            return Objects.hash(movieId, numberOfDays, bonusPoints);
        }

        @Override
        public String toString() {
            return "Rental{" +
                    "movieId=" + movieId +
                    ", numberOfDays=" + numberOfDays +
                    ", bonusPoints=" + bonusPoints +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateOrderRentalsRequest)) return false;
        CreateOrderRentalsRequest that = (CreateOrderRentalsRequest) o;
        return Objects.equals(customerId, that.customerId) &&
                Objects.equals(rentals, that.rentals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, rentals);
    }

    @Override
    public String toString() {
        return "CreateOrderRentalsRequest{" +
                "customerId=" + customerId +
                ", rentals=" + rentals +
                '}';
    }
}