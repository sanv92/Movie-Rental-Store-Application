package com.example.movierentalstoreapplication.model.movie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldHaveNoViolationsMin() {
        Movie movie = new Movie(
                repeat(3),
                repeat(160),
                1992,
                MovieType.OLD
        );

        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldHaveNoViolationsMax() {
        Movie movie = new Movie(
                repeat(255),
                repeat(9999),
                1992,
                MovieType.OLD
        );

        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldDetectInvalidTitleMin() {
        Movie movie = new Movie(
                repeat(2),
                repeat(160),
                1992,
                MovieType.OLD
        );

        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);
        ConstraintViolation<Movie> violation = violations.iterator().next();

        assertEquals(1, violations.size());
        assertEquals(Movie_.TITLE, violation.getPropertyPath().toString());
    }

    @Test
    public void shouldDetectInvalidTitleMax() {
        Movie movie = new Movie(
                repeat(256),
                repeat(160),
                1992,
                MovieType.OLD
        );

        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);
        ConstraintViolation<Movie> violation = violations.iterator().next();

        assertEquals(1, violations.size());
        assertEquals(Movie_.TITLE, violation.getPropertyPath().toString());
    }

    @Test
    public void shouldDetectInvalidDescription() {
        Movie movie = new Movie(
                repeat(3),
                repeat(159),
                1992,
                MovieType.OLD
        );

        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);
        ConstraintViolation<Movie> violation = violations.iterator().next();

        assertEquals(1, violations.size());
        assertEquals(Movie_.DESCRIPTION, violation.getPropertyPath().toString());
    }

    @Test
    public void shouldDetectInvalidYear() {
        Movie movie = new Movie(
                repeat(3),
                repeat(160),
                1969,
                MovieType.OLD
        );

        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);
        ConstraintViolation<Movie> violation = violations.iterator().next();

        assertEquals(1, violations.size());
        assertEquals(Movie_.YEAR, violation.getPropertyPath().toString());
    }

    @Test
    public void shouldDetectInvalidMovieType() {
        Movie movie = new Movie(
                repeat(3),
                repeat(160),
                1992,
                null
        );

        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);
        ConstraintViolation<Movie> violation = violations.iterator().next();

        assertEquals(1, violations.size());
        assertEquals(Movie_.TYPE, violation.getPropertyPath().toString());
    }

    private static String repeat(int times) {
        return StringUtils.repeat("*", times);
    }
}