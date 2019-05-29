package com.example.movierentalstoreapplication.controllers;

import com.example.movierentalstoreapplication.dtos.MovieDto;
import com.example.movierentalstoreapplication.dtos.MovieOrderDto;
import com.example.movierentalstoreapplication.dtos.MovieRentalDto;
import com.example.movierentalstoreapplication.model.Customer;
import com.example.movierentalstoreapplication.model.movie.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

abstract class AbstractOrderControllerIntegrationTest extends MockMvcIntegrationTest {
    protected static double createPrice() {
        return MovieType.OLD.calculatePrice();
    }

    protected static int createPoints() {
        return MovieType.OLD.calculatePoints();
    }

    protected static Customer createCustomer() {
        return new Customer("First Name", "Last Name");
    }

    protected static Movie createMovie() {
        return new Movie("Title", "Description", 1992, MovieType.OLD);
    }

    protected static MovieDto createMovieDto() {
        return new MovieDto()
                .setTitle("Title")
                .setDescription("Description")
                .setYear(1992)
                .setType(MovieType.OLD.name())
                .setPrice(createPrice())
                .setPoints(createPoints())
                .setPublished(true);
    }

    protected static MovieRental createMovieRental() {
        return new MovieRental(new MovieOrder(), createMovie(), 1, 2, MovieRental.Status.RETURNED)
                .setPriceType(PriceType.PREMIUM)
                .setPrice(1.0)
                .setPickupDate(LocalDateTime.of(1992, 7, 11, 0, 0))
                .setReturnDate(LocalDateTime.of(1992, 7, 11, 0, 1))
                .setLateCharge(3.0)
                .setPoints(4);
    }

    protected static MovieRentalDto createMovieRentalDto() {
        return new MovieRentalDto()
                .setMovie(createMovieDto())
                .setNumberOfDays(1)
                .setPoints(4)
                .setStatus(MovieRental.Status.RETURNED.name())
                .setPriceType(PriceType.PREMIUM.name())
                .setPrice(1.0)
                .setPickupDate(LocalDateTime.of(1992, 7, 11, 0, 0))
                .setReturnDate(LocalDateTime.of(1992, 7, 11, 0, 1))
                .setLateCharge(3.0)
                .setPriceForOneDay(3.0)
                .setPriceForAllDay(3.0)
                .setDiscount(0.0)
                .setExtraDays(0);
    }

    protected static List<MovieRental> createOrderRentals() {
        return Arrays.asList(
                createMovieRental(),
                createMovieRental(),
                createMovieRental()
        );
    }

    protected static List<MovieRentalDto> createOrderRentalsDto() {
        return Arrays.asList(
                createMovieRentalDto(),
                createMovieRentalDto(),
                createMovieRentalDto()
        );
    }

    protected static MovieOrder createMovieOrder() {
        return new MovieOrder(createCustomer(), MovieOrder.Status.OPENED, createOrderRentals());
    }

    protected static MovieOrder createMovieOrder(Customer customer) {
        return new MovieOrder(customer, MovieOrder.Status.OPENED, createOrderRentals());
    }

    protected static MovieOrder createMovieOrder(Customer customer, MovieOrder.Status status) {
        return new MovieOrder(customer, status, createOrderRentals());
    }

    protected static MovieOrder createMovieOrder(Customer customer, MovieOrder.Status status, List<MovieRental> rentals) {
        return new MovieOrder(customer, status, rentals);
    }

    protected static MovieOrderDto createMovieOrderDto() {
        return new MovieOrderDto()
                .setStatus(MovieOrder.Status.OPENED.name())
                .setRentals(createOrderRentalsDto())
                .setTotalLateCharge(9.0)
                .setTotalPoints(12)
                .setTotalPrice(3.0)
                .setTotalPriceForAllDay(9.0)
                .setTotalDiscount(0.0)
                .setTotalPriceWithLateCharge(18.0);
    }

    protected static MovieOrderDto createMovieOrderDto(String title, String description, Integer year, Boolean published, String movieType) {
        return new MovieOrderDto()
                .setStatus(MovieOrder.Status.OPENED.name())
                .setRentals(createOrderRentalsDto())
                .setTotalLateCharge(9.0)
                .setTotalPoints(12)
                .setTotalPrice(3.0)
                .setTotalPriceWithLateCharge(18.0);
    }
}
