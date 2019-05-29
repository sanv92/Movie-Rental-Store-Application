package com.example.movierentalstoreapplication.model.services;

import com.example.movierentalstoreapplication.model.Customer;
import com.example.movierentalstoreapplication.model.movie.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MovieRentalServiceTest {

    private MovieRentalService movieRentalService;
    private Customer customer;

    @BeforeEach
    void setUp() {
        this.movieRentalService = new MovieRentalService();
        this.customer = new Customer()
                .depositBalance(10)
                .depositPoints(25);
    }

    @Test
    @DisplayName("Should be rented for money")
    void rentMovieForMoney() {
        MovieRental movieRental = createRent(1, 0);
        MovieRental rent = movieRentalService.rentMovie(customer, movieRental);

        assertEquals(MovieRental.Status.ONGOING, rent.getStatus());
        assertEquals(PriceType.PREMIUM, rent.getPriceType());
        assertEquals(MovieType.NEW.getPriceType().getPrice(), rent.getPrice());
        assertEquals(0, rent.getPoints());
    }

    @Test
    @DisplayName("Should be rented for bonus points")
    void rentMovieForPoints() {
        MovieRental movieRental = createRent(1, PointsType.PREMIUM.getPrice());
        MovieRental rent = movieRentalService.rentMovie(customer, movieRental);

        assertEquals(MovieRental.Status.ONGOING, rent.getStatus());
        assertEquals(PriceType.PREMIUM, rent.getPriceType());
        assertEquals(0, rent.getPrice());
        assertEquals(25, rent.getPoints());
    }

    @Test
    @DisplayName("Check customer balance and points, when pay for money")
    void customerDepositWhen_RentForMoney() {
        MovieRental movieRental = createRent(1, 0);
        movieRentalService.rentMovie(customer, movieRental);

        assertEquals(6, customer.getBalance());
        assertEquals(25, customer.getBonusPoints());
    }

    @Test
    @DisplayName("Check customer balance and points, when pay for points")
    void checkCustomerDepositWhen_RentForPoints() {
        MovieRental movieRental = createRent(1, PointsType.PREMIUM.getPrice());
        movieRentalService.rentMovie(customer, movieRental);

        assertEquals(10, customer.getBalance());
        assertEquals(0, customer.getBonusPoints());
    }

    @Test
    @DisplayName("Should throw exception, when movie already rented")
    void throwExceptionWhen_MovieAlreadyRented() {
        MovieRental movieRental = createRent(1, 0, MovieRental.Status.ONGOING);

        assertThrows(MovieRentalService.MovieAlreadyRentedException.class,
                () -> {
                    movieRentalService.rentMovie(customer, movieRental);
                }
        );
    }

    @Test
    @DisplayName("Should throw exception, when movie rental days is less then 1")
    void throwExceptionWhen_MovieRentalIsLessThenOne() {
        MovieRental movieRental = createRent(0, 0);

        assertThrows(MovieRentalService.MinimumDaysRentException.class,
                () -> movieRentalService.rentMovie(customer, movieRental)
        );
    }

    @Test
    @DisplayName("Should throw exception, when customer money balance is less then 0")
    void throwExceptionWhen_RentMovieAndCustomerBalanceIsNegative() {
        MovieRental movieRental = new MovieRental(createOrder(), createMovie(), Integer.MAX_VALUE, 0);

        assertThrows(MovieRentalService.NotEnoughBalanceException.class,
                () -> {
                    movieRentalService.rentMovie(customer, movieRental);
                }
        );
    }

    @Test
    @DisplayName("Should throw exception, when rent a movie and customer bonus balance is less then 0")
    void throwExceptionWhen_RentMovieAndCustomerBonusPointsIsNegative() {
        MovieRental movieRental1 = createRent(1, 26);
        MovieRental movieRental2 = createRent(1, 50);

        assertThrows(MovieRentalService.NotEnoughBonusPointsException.class,
                () -> movieRentalService.rentMovie(customer, movieRental1)
        );
        assertThrows(MovieRentalService.NotEnoughBonusPointsException.class,
                () -> movieRentalService.rentMovie(customer, movieRental2)
        );
    }

    @Test
    @DisplayName("Should be returned movie")
    void returnMovie() {
        MovieRental movieRental = createRent(1, 0);

        MovieRental rental = movieRentalService.returnMovie(customer,
                movieRentalService.rentMovie(customer, movieRental)
        );

        assertEquals(MovieRental.Status.RETURNED, rental.getStatus());
        assertNotNull(rental.getReturnDate());
        assertEquals(0, rental.getLateCharge());
    }

    @Test
    @DisplayName("Should return the amount of debt, when movie was returned late")
    void returnAmountOfDebtWhen_MovieReturnedLate() {
        MovieRental movieRental = createRent(1, 0)
                .setPickupDate(LocalDateTime.now().minusDays(2));
        Customer customer = new Customer()
                .depositBalance(100);

        MovieRental rental = movieRentalService.returnMovie(customer,
                movieRentalService.rentMovie(customer, movieRental)
        );

        assertNotNull(rental.getReturnDate());
        assertEquals(4, rental.getLateCharge());
    }

    @Test
    @DisplayName("Check customer balance, when movie returns on time")
    void checkCustomerBalanceWhen_MovieReturnOnTime() {
        MovieRental movieRental = createRent(1, 0);

        movieRentalService.returnMovie(customer,
                movieRentalService.rentMovie(customer, movieRental)
        );

        assertEquals(6, customer.getBalance());
    }

    @Test
    @DisplayName("Check customer balance, when movie returns late")
    void checkCustomerBalanceWhen_MovieReturnLate() {
        MovieRental movieRental = new MovieRental(createOrder(), createMovie(), 1, 0)
                .setPickupDate(LocalDateTime.now().minusDays(2));

        MovieRental rental = movieRentalService.returnMovie(customer,
                movieRentalService.rentMovie(customer, movieRental)
        );

        assertEquals(4, rental.getLateCharge());
    }

    @Test
    @DisplayName("Should close the order, if the last rented movie is returned")
    void closeOrderIf_LastRentedMovieIsReturned() {
        MovieRental movieRental = createRent(1, 0);

        MovieRental rental = movieRentalService.returnMovie(customer,
                movieRentalService.rentMovie(customer, movieRental)
        );

        assertEquals(MovieRental.Status.RETURNED, rental.getStatus());
        assertEquals(MovieOrder.Status.CLOSED, rental.getOrder().getStatus());
    }

    @Test
    @DisplayName("Should throw exception, when customer balance is less then 0")
    void throwExceptionWhen_ReturnMovieAndCustomerBalanceIsNegative() {
        MovieRental rental = new MovieRental(createOrder(), createMovie(), 1, 0)
                .setPickupDate(LocalDateTime.now().minusDays(Integer.MAX_VALUE));

        System.out.println("rental: " + rental);
        assertThrows(MovieRentalService.NotEnoughBalanceException.class,
                () -> movieRentalService.returnMovie(customer, rental)
        );
    }

    @Test
    @DisplayName("Should throw exception, when return a movie and movie is already returned")
    void throwExceptionWhen_ReturnAlreadyReturnedMovie() {
        assertThrows(MovieRentalService.MovieAlreadyReturnedException.class,
                () -> movieRentalService.returnMovie(customer,
                        createRent(1, 0, MovieRental.Status.RETURNED)
                )
        );
        assertThrows(MovieRentalService.MovieAlreadyReturnedException.class,
                () -> movieRentalService.returnMovie(customer,
                        createRent(1, 0, MovieRental.Status.RETURNED, LocalDateTime.now())
                )
        );
    }

    @Test
    @DisplayName("Should throw exception, when return and rental list is empty")
    void throwExceptionWhen_ReturnAndRentalListIsEmpty() {
        MovieRental movieRental = createRent(1, 0)
                .setOrder(
                        new MovieOrder()
                                .setRentals(new ArrayList<>())
                );

        assertThrows(NoSuchElementException.class,
                () -> movieRentalService.returnMovie(customer,
                        movieRentalService.rentMovie(customer, movieRental)
                )
        );
    }

    private static MovieOrder createOrder() {
        return new MovieOrder()
                .setRentals(
                        Collections.singletonList(
                                new MovieRental()
                                        .setStatus(MovieRental.Status.ONGOING)
                        )
                );
    }

    private static Movie createMovie() {
        return new Movie("Title", "Description", 1992, MovieType.NEW);
    }

    private static MovieRental createRent(int numberOfDays, int pointsPrice) {
        return new MovieRental(createOrder(), createMovie(), numberOfDays, pointsPrice);
    }

    private static MovieRental createRent(int numberOfDays, int pointsPrice, MovieRental.Status status) {
        return new MovieRental(createOrder(), createMovie(), numberOfDays, pointsPrice, status);
    }

    private static MovieRental createRent(int numberOfDays, int pointsPrice, MovieRental.Status status, LocalDateTime returnDate) {
        MovieRental movieRental = new MovieRental(createOrder(), createMovie(), numberOfDays, pointsPrice, status);

        movieRental.setReturnDate(returnDate);

        return movieRental;
    }
}