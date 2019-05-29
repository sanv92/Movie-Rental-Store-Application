package com.example.movierentalstoreapplication.model.movie;

import com.example.movierentalstoreapplication.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MovieOrderTest {

    @Test
    void isOpened() {
        MovieOrder movieOrder = new MovieOrder()
                .setStatus(MovieOrder.Status.OPENED);

        assertTrue(movieOrder.isOpened());
    }

    @Test
    void isClosed() {
        MovieOrder movieOrder = new MovieOrder()
                .setStatus(MovieOrder.Status.CLOSED);

        assertTrue(movieOrder.isClosed());
    }

    @Test
    void calculateTotalPrice() {
        assertEquals(6, createMovieRentOrder().calculateTotalPrice());
    }

    @Test
    void calculateTotalPriceByDays() {
        assertEquals(24, createMovieRentOrder().calculateTotalPriceByDays());
    }

    @Test
    void calculateTotalDiscount() {
        assertEquals(12, createMovieRentOrder().calculateTotalDiscount());
    }

    @Test
    void calculateTotalPriceWithDiscount() {
        assertEquals(12, createMovieRentOrder().calculateTotalPriceByDaysWithDiscount());
    }

    @Test
    void calculateTotalPoints() {
        assertEquals(75, createMovieRentOrder().calculateTotalPoints());
    }

    @Test
    void calculateTotalLateCharge() {
        assertEquals(9, createMovieRentOrder().calculateTotalLateCharge());
    }

    @Test
    void calculateTotalPriceWithLateCharge() {
        assertEquals(21, createMovieRentOrder().calculateTotalPriceWithLateCharge());
    }

    @Nested
    @DisplayName("Validation")
    class Validation {
        private Validator validator;

        private MovieOrder movieOrder;

        @BeforeEach
        public void setUp() {
            ValidatorFactory factory = javax.validation.Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();

            this.movieOrder = createMovieRentOrder();
        }

        @Test
        void shouldHaveNoViolations() {
            Set<ConstraintViolation<MovieOrder>> violations = validator.validate(movieOrder);

            assertTrue(violations.isEmpty());
        }

        @Test
        void shouldDetectInvalidCustomer() {
            Set<ConstraintViolation<MovieOrder>> violations = validator.validate(createMovieRentOrder(null));

            ConstraintViolation<MovieOrder> violation = violations.iterator().next();

            assertEquals(1, violations.size());
            assertEquals(MovieOrder_.CUSTOMER, violation.getPropertyPath().toString());
        }

        @Test
        void shouldDetectInvalidStatus() {
            Set<ConstraintViolation<MovieOrder>> violations = validator.validate(createMovieRentOrder(createCustomer(), null));

            ConstraintViolation<MovieOrder> violation = violations.iterator().next();

            assertEquals(1, violations.size());
            assertEquals(MovieOrder_.STATUS, violation.getPropertyPath().toString());
        }

        @Test
        void shouldDetectInvalidRentals() {
            List<MovieRental> movieRentals = new ArrayList<>();
            Set<ConstraintViolation<MovieOrder>> violations = validator.validate(createMovieRentOrder(
                    createCustomer(),
                    createStatus(),
                    movieRentals
            ));

            ConstraintViolation<MovieOrder> violation = violations.iterator().next();

            assertEquals(1, violations.size());
            assertEquals(MovieOrder_.RENTALS, violation.getPropertyPath().toString());
        }
    }

    private static Customer createCustomer() {
        return new Customer("First Name", "Last Name");
    }

    private static Movie createMovie() {
        return new Movie("Title", "Description", 2019, MovieType.NEW);
    }

    private static MovieOrder.Status createStatus() {
        return MovieOrder.Status.OPENED;
    }

    private static MovieRental createMovieRental() {
        return new MovieRental(new MovieOrder(), createMovie(), 2, 25)
                .setPrice(2)
                .setLateCharge(3);
    }

    private static MovieOrder createMovieRentOrder() {
        return new MovieOrder(
                createCustomer(),
                createStatus()
        )
                .setRentals(
                        Arrays.asList(createMovieRental(), createMovieRental(), createMovieRental())
                );
    }

    private static MovieOrder createMovieRentOrder(Customer customer) {
        return new MovieOrder(
                customer,
                createStatus()
        )
                .setRentals(
                        Arrays.asList(createMovieRental(), createMovieRental(), createMovieRental())
                );
    }

    private static MovieOrder createMovieRentOrder(Customer customer, MovieOrder.Status status) {
        return new MovieOrder(
                customer,
                status
        )
                .setRentals(
                        Arrays.asList(createMovieRental(), createMovieRental(), createMovieRental())
                );
    }

    private static MovieOrder createMovieRentOrder(Customer customer, MovieOrder.Status status, List<MovieRental> movieRentals) {
        return new MovieOrder(
                customer,
                status
        )
                .setRentals(movieRentals);
    }
}