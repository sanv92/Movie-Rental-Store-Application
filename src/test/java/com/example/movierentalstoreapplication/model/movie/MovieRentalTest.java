package com.example.movierentalstoreapplication.model.movie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MovieRentalTest {

    @Test
    @DisplayName("Should create MovieRental, without any problem")
    void testMovieRental() {
        LocalDateTime localDateTime = LocalDateTime.now();
        MovieRental movieRental = new MovieRental(
                new MovieOrder(),
                new Movie(),
                3,
                2
        )
                .setPrice(1)
                .setPickupDate(localDateTime.minusDays(5))
                .setReturnDate(localDateTime)
                .setStatus(MovieRental.Status.ONGOING)
                .setPriceType(PriceType.PREMIUM)
                .setLateCharge(4);

        assertEquals(1, movieRental.getPrice());
        assertEquals(2, movieRental.getPoints());
        assertEquals(localDateTime.minusDays(5), movieRental.getPickupDate());
        assertEquals(localDateTime, movieRental.getReturnDate());
        assertEquals(MovieRental.Status.ONGOING, movieRental.getStatus());
        assertEquals(3, movieRental.getNumberOfDays());
        assertEquals(PriceType.PREMIUM, movieRental.getPriceType());
        assertEquals(4, movieRental.getLateCharge());
    }

    @Test
    @DisplayName("Should create MovieRental with default properties")
    void testBuildDefaultProperties() {
        MovieRental movieRental = new MovieRental();

        assertEquals(MovieRental.Status.UNKNOWN, movieRental.getStatus());
        assertEquals(0.0, movieRental.getPrice());
        assertEquals(0, movieRental.getLateCharge());
        assertEquals(0, movieRental.getPoints());
        assertNotNull(movieRental.getPickupDate());
    }

    @Test
    @DisplayName("Checks ongoing status")
    void isReturned() {
        MovieRental movieRental = createOngoingMovieRental();

        assertFalse(movieRental.isReturned());
        assertTrue(movieRental.isOngoing());
    }

    @Test
    @DisplayName("Checks returned status")
    void isOngoing() {
        MovieRental movieRental = createReturnedMovieRental();

        assertTrue(movieRental.isReturned());
        assertFalse(movieRental.isOngoing());
    }

    @Test
    @DisplayName("Should return extra days, if the movie was returned to late")
    void calculateExtraDays() {
        assertEquals(
                9,
                createOngoingMovieRental(createMovieOrder(), createMovie(), 1)
                        .setPickupDate(LocalDateTime.now().minusDays(10))
                        .setReturnDate(LocalDateTime.now().minusDays(5))
                        .calculateExtraDays()
        );
        assertEquals(
                4,
                createReturnedMovieRental(createMovieOrder(), createMovie(), 1)
                        .setPickupDate(LocalDateTime.now().minusDays(10))
                        .setReturnDate(LocalDateTime.now().minusDays(5))
                        .calculateExtraDays()
        );
    }

    @Test
    @DisplayName("Should return correct return date")
    void calculateReturnDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        int days = 5;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(days);
        MovieRental movieRental = createOngoingMovieRental(createMovieOrder(), createMovie(), days)
                .setPickupDate(startDate)
                .setReturnDate(endDate);

        assertEquals(endDate.format(formatter), movieRental.calculateReturnDate().format(formatter));
    }

    @Test
    @DisplayName("Should return the amount of debt, if the movie was returned late and status is ongoing")
    void calculateLateChargeWhenOngoing() {
        assertEquals(8, createOngoingMovieRental().calculateLateCharge());
    }

    @Test
    @DisplayName("Should return the amount of debt, if the movie was returned late and status is returned")
    void calculateLateChargeWhenReturned() {
        assertEquals(99, createReturnedMovieRental().calculateLateCharge());
    }

    @Test
    @DisplayName("Calculate price for single days")
    void calculatePriceForSingleDay() {
        assertEquals(PriceType.PREMIUM.getPrice(), createReturnedMovieRental().calculatePrice());
    }

    @Test
    @DisplayName("Calculate price by days")
    void calculatePriceByDays() {
        double expectedResult = 12;
        double result = createReturnedMovieRental(createMovieOrder(), createMovie(), 3).calculatePriceByDays();

        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Calculate discount")
    void calculateDiscount() {
        double expectedResult = 4;
        double result = createReturnedMovieRental(createMovieOrder(), createMovie(), 3, 25).calculateDiscount();

        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Calculate price with discount")
    void calculatePriceByDaysWithDiscount() {
        double expectedResult = 8;
        double result = createReturnedMovieRental(createMovieOrder(), createMovie(), 3, 25).calculatePriceByDaysWithDiscount();

        assertEquals(expectedResult, result);
    }

    @Nested
    @DisplayName("Validation")
    class Validation {
        private Validator validator;

        @BeforeEach
        public void setUp() {
            ValidatorFactory factory = javax.validation.Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }

        @Test
        void shouldHaveNoViolations() {
            Set<ConstraintViolation<MovieRental>> violations = validator.validate(createOngoingMovieRental());

            assertTrue(violations.isEmpty());
        }

        @Test
        void shouldDetectInvalidOrder() {
            MovieRental movieRental = createOngoingMovieRental(null, createMovie());
            Set<ConstraintViolation<MovieRental>> violations = validator.validate(movieRental);

            ConstraintViolation<MovieRental> violation = violations.iterator().next();

            assertEquals(1, violations.size());
            assertEquals(MovieRental_.ORDER, violation.getPropertyPath().toString());
        }

        @Test
        void shouldDetectInvalidMovie() {
            MovieRental movieRental = createOngoingMovieRental(createMovieOrder(), null);
            Set<ConstraintViolation<MovieRental>> violations = validator.validate(movieRental);

            ConstraintViolation<MovieRental> violation = violations.iterator().next();

            assertEquals(1, violations.size());
            assertEquals(MovieRental_.MOVIE, violation.getPropertyPath().toString());
        }

        @Test
        void shouldDetectInvalidStatus() {
            MovieRental movieRental = createOngoingMovieRental(createMovieOrder(), createMovie())
                    .setStatus(null);
            Set<ConstraintViolation<MovieRental>> violations = validator.validate(movieRental);

            ConstraintViolation<MovieRental> violation = violations.iterator().next();

            assertEquals(1, violations.size());
            assertEquals(MovieRental_.STATUS, violation.getPropertyPath().toString());
        }

        @Test
        void shouldDetectInvalidPriceType() {
            MovieRental movieRental = createOngoingMovieRental(createMovieOrder(), createMovie())
                    .setPriceType(null);
            Set<ConstraintViolation<MovieRental>> violations = validator.validate(movieRental);

            ConstraintViolation<MovieRental> violation = violations.iterator().next();

            assertEquals(1, violations.size());
            assertEquals(MovieRental_.PRICE_TYPE, violation.getPropertyPath().toString());
        }

        @Test
        void shouldDetectInvalidPriceAndLateChargeAndPoints() {
            MovieRental movieRental = createOngoingMovieRental(createMovieOrder(), createMovie())
                    .setLateCharge(-1)
                    .setPrice(-1)
                    .setPoints(-1);
            Set<ConstraintViolation<MovieRental>> violations = validator.validate(movieRental);

            ConstraintViolation<MovieRental> violation = violations.iterator().next();

            assertEquals(3, violations.size());
        }

        @Test
        void shouldDetectInvalidNumberOfDays() {
            assertThrows(NullPointerException.class,
                    () -> createOngoingMovieRental(createMovieOrder(), createMovie(), null)
            );
        }

        @Test
        void shouldDetectInvalidPickupDatePast() {
            MovieRental movieRental = createOngoingMovieRental(createMovieOrder(), createMovie())
                    .setPickupDate(LocalDateTime.now().minusDays(1));
            Set<ConstraintViolation<MovieRental>> violations = validator.validate(movieRental);

            assertEquals(0, violations.size());
        }

        @Test
        void shouldDetectInvalidPickupDateFuture() {
            MovieRental movieRental = createOngoingMovieRental(createMovieOrder(), createMovie())
                    .setPickupDate(LocalDateTime.now().plusDays(1));
            Set<ConstraintViolation<MovieRental>> violations = validator.validate(movieRental);

            ConstraintViolation<MovieRental> violation = violations.iterator().next();

            assertEquals(1, violations.size());
            assertEquals(MovieRental_.PICKUP_DATE, violation.getPropertyPath().toString());
        }

        @Test
        void shouldDetectInvalidReturnDatePast() {
            MovieRental movieRental = createOngoingMovieRental(createMovieOrder(), createMovie())
                    .setPickupDate(LocalDateTime.now().minusDays(1));
            Set<ConstraintViolation<MovieRental>> violations = validator.validate(movieRental);

            assertEquals(0, violations.size());
        }

        @Test
        void shouldDetectInvalidReturnDateFuture() {
            MovieRental movieRental = createOngoingMovieRental(createMovieOrder(), createMovie())
                    .setPickupDate(LocalDateTime.now().plusDays(1));
            Set<ConstraintViolation<MovieRental>> violations = validator.validate(movieRental);

            ConstraintViolation<MovieRental> violation = violations.iterator().next();

            assertEquals(1, violations.size());
            assertEquals(MovieRental_.PICKUP_DATE, violation.getPropertyPath().toString());
        }
    }

    private static MovieOrder createMovieOrder() {
        return new MovieOrder();
    }

    private static Movie createMovie() {
        return new Movie("Title", "Description", 1992, MovieType.NEW);
    }

    private static MovieRental.Status createOngoingStatus() {
        return MovieRental.Status.ONGOING;
    }

    private static MovieRental.Status createReturnedStatus() {
        return MovieRental.Status.RETURNED;
    }

    private static MovieRental createOngoingMovieRental() {
        return new MovieRental(
                createMovieOrder(),
                createMovie(),
                1,
                0
        )
                .setStatus(createOngoingStatus())
                .setPriceType(PriceType.PREMIUM)
                .setPrice(12)
                .setLateCharge(99)
                .setPickupDate(LocalDateTime.now().minusDays(5))
                .setReturnDate(LocalDateTime.now().minusDays(3));
    }

    private static MovieRental createReturnedMovieRental() {
        return new MovieRental(
                createMovieOrder(),
                createMovie(),
                1,
                0
        )
                .setStatus(createReturnedStatus())
                .setPriceType(PriceType.PREMIUM)
                .setPrice(12)
                .setLateCharge(99)
                .setPickupDate(LocalDateTime.now().minusDays(5))
                .setReturnDate(LocalDateTime.now().minusDays(3));
    }

    private static MovieRental createOngoingMovieRental(MovieOrder movieOrder, Movie movie) {
        return new MovieRental(
                movieOrder,
                movie,
                1,
                0
        )
                .setStatus(createOngoingStatus())
                .setPriceType(PriceType.PREMIUM)
                .setPrice(12)
                .setLateCharge(99)
                .setPickupDate(LocalDateTime.now().minusDays(5))
                .setReturnDate(LocalDateTime.now().minusDays(3));
    }

    private static MovieRental createOngoingMovieRental(MovieOrder movieOrder, Movie movie, Integer numberOfDays) {
        return new MovieRental(
                movieOrder,
                movie,
                numberOfDays,
                0
        )
                .setStatus(createOngoingStatus())
                .setPriceType(PriceType.PREMIUM)
                .setPrice(12)
                .setLateCharge(99)
                .setPickupDate(LocalDateTime.now().minusDays(5))
                .setReturnDate(LocalDateTime.now().minusDays(3));
    }

    private static MovieRental createReturnedMovieRental(MovieOrder movieOrder, Movie movie, Integer numberOfDays) {
        return new MovieRental(
                movieOrder,
                movie,
                numberOfDays,
                0
        )
                .setStatus(createReturnedStatus())
                .setPriceType(PriceType.PREMIUM)
                .setPrice(12)
                .setLateCharge(99)
                .setPickupDate(LocalDateTime.now().minusDays(5))
                .setReturnDate(LocalDateTime.now().minusDays(3));
    }

    private static MovieRental createReturnedMovieRental(MovieOrder movieOrder, Movie movie, Integer numberOfDays, Integer points) {
        return new MovieRental(
                movieOrder,
                movie,
                numberOfDays,
                points
        )
                .setStatus(createReturnedStatus())
                .setPriceType(PriceType.PREMIUM)
                .setPrice(12)
                .setLateCharge(99)
                .setPickupDate(LocalDateTime.now().minusDays(5))
                .setReturnDate(LocalDateTime.now().minusDays(3));
    }
}