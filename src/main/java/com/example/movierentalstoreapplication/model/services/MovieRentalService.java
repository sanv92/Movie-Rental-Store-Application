package com.example.movierentalstoreapplication.model.services;

import com.example.movierentalstoreapplication.model.Customer;
import com.example.movierentalstoreapplication.model.movie.MovieOrder;
import com.example.movierentalstoreapplication.model.movie.MovieRental;
import com.example.movierentalstoreapplication.model.movie.PriceType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MovieRentalService {

    static final class MovieAlreadyRentedException extends RuntimeException {
        MovieAlreadyRentedException() {
            super();
        }

        @Override
        public String getMessage() {
            return "Cannot rent movie, already rented movie";
        }
    }

    static final class MovieAlreadyReturnedException extends RuntimeException {
        MovieAlreadyReturnedException() {
            super();
        }

        @Override
        public String getMessage() {
            return "Cannot return movie, already returned movie";
        }
    }

    static final class MinimumDaysRentException extends RuntimeException {
        MinimumDaysRentException() {
            super();
        }

        @Override
        public String getMessage() {
            return "The minimum days to rent is one day";
        }
    }

    static final class NotEnoughBalanceException extends RuntimeException {
        NotEnoughBalanceException() {
            super();
        }

        @Override
        public String getMessage() {
            return "Not enough balance";
        }
    }

    static final class NotEnoughBonusPointsException extends RuntimeException {
        NotEnoughBonusPointsException() {
            super();
        }

        @Override
        public String getMessage() {
            return "Not enough bonus points";
        }
    }

    public MovieRental rentMovie(Customer customer, MovieRental movieRental) {
        if (movieRental.isOngoing()) {
            throw new MovieAlreadyRentedException();
        }

        if (lessThanOneDay(movieRental.getNumberOfDays())) {
            throw new MinimumDaysRentException();
        }

        if (ifNotEnoughBonusPoints(customer, movieRental.getPoints())) {
            throw new NotEnoughBonusPointsException();
        }

        MovieRental rental = this.calculateRentMovie(movieRental);

        if (ifNotEnoughBalance(customer, rental.getPrice())) {
            throw new NotEnoughBalanceException();
        }

        customer.withdrawBalance(rental.getPrice())
                .withdrawPoints(rental.getPoints());

        return rental;
    }

    public MovieRental calculateRentMovie(MovieRental movieRental) {
        PriceType priceType = movieRental.getMovie().getType().getPriceType();
        double moviePrice = movieRental.calculatePriceByDaysWithDiscount();
        int movieBonusPoints = movieRental.getMovie().getType().getPointsType().calculatePrice(movieRental.getPoints());

        return movieRental
                .setStatus(MovieRental.Status.ONGOING)
                .setPriceType(priceType)
                .setPrice(moviePrice)
                .setPoints(movieBonusPoints);
    }

    public MovieRental returnMovie(Customer customer, MovieRental movieRental) {
        if (movieRental.isReturned() || movieRental.getReturnDate() != null) {
            throw new MovieAlreadyReturnedException();
        }

        List<MovieRental> rentals = movieRental.getOrder().getRentals();
        if (rentals.isEmpty()) {
            throw new NoSuchElementException();
        }

        MovieRental rental = this.calculateReturnMovie(movieRental);

        if (rental.getLateCharge() > 0) {
            if (ifNotEnoughBalance(customer, rental.getLateCharge())) {
                throw new NotEnoughBalanceException();
            }

            customer.withdrawBalance(rental.getLateCharge());
        }

        return rental;
    }

    public MovieRental calculateReturnMovie(MovieRental movieRental) {
        double lateCharge = movieRental.calculateLateCharge();
        MovieOrder movieOrder = this.closeTheOrderIfLastRental(movieRental);

        return movieRental
                .setStatus(MovieRental.Status.RETURNED)
                .setReturnDate(LocalDateTime.now())
                .setLateCharge(lateCharge)
                .setOrder(movieOrder);
    }

    private boolean lessThanOneDay(int days) {
        return days <= 0;
    }

    private boolean ifNotEnoughBonusPoints(Customer customer, int points) {
        return customer.getBonusPoints() - points < 0;
    }

    private boolean ifNotEnoughBalance(Customer customer, double price) {
        return customer.getBalance() - price < 0;
    }

    private boolean isLastRental(List<MovieRental> rentalList) {
        return rentalList.stream().filter(rental -> rental.getStatus().equals(MovieRental.Status.ONGOING))
                .count() == 1;
    }

    private MovieOrder closeTheOrderIfLastRental(MovieRental movieRental) {
        MovieOrder movieOrder = movieRental.getOrder();

        if (isLastRental(movieOrder.getRentals())) {
            return movieOrder
                    .setStatus(MovieOrder.Status.CLOSED);
        }

        return movieOrder;
    }
}
