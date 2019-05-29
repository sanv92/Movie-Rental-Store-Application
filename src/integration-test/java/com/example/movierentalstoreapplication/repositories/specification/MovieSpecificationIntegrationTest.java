package com.example.movierentalstoreapplication.repositories.specification;

import com.example.movierentalstoreapplication.model.Customer;
import com.example.movierentalstoreapplication.model.movie.*;
import com.example.movierentalstoreapplication.repositories.CustomerRepository;
import com.example.movierentalstoreapplication.repositories.MovieOrderRepository;
import com.example.movierentalstoreapplication.repositories.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.springframework.data.jpa.domain.Specification.where;
import static com.example.movierentalstoreapplication.repositories.specification.MovieSpecification.*;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thymeleaf.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class MovieSpecificationIntegrationTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieOrderRepository movieOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        List<Movie> movies = movieRepository.saveAll(
                Arrays.asList(
                        new Movie("Title1", repeat(160), 1992, MovieType.NEW, true),
                        new Movie("Title2", repeat(160), 1993, MovieType.REGULAR, true),
                        new Movie("Title3", repeat(160), 1994, MovieType.OLD, false)
                )
        );

        Customer customer = customerRepository.save(
                new Customer("First name", "Last name", 0, 0)
        );

        MovieOrder movieOrder = new MovieOrder(customer, MovieOrder.Status.OPENED);

        movieOrder.setRentals(
                Arrays.asList(
                        new MovieRental(movieOrder, movies.get(0), 1, 0, MovieRental.Status.ONGOING)
                                .setPriceType(PriceType.PREMIUM),
                        new MovieRental(movieOrder, movies.get(1), 1, 0, MovieRental.Status.ONGOING)
                                .setPriceType(PriceType.PREMIUM),
                        new MovieRental(movieOrder, movies.get(2), 1, 0, MovieRental.Status.RETURNED)
                                .setPriceType(PriceType.PREMIUM)
                )
        );

        movieOrderRepository.save(movieOrder);
    }


    @Test
    void testIsPublishedMovie() {
        assertEquals(3, movieRepository
                .findAll(where(isPublishedMovie(null))).size());
        assertEquals(2, movieRepository
                .findAll(where(isPublishedMovie(true))).size());
        assertEquals(1, movieRepository
                .findAll(where(isPublishedMovie(false))).size());
    }

    @Test
    void testFilterByMovieType() {
        assertEquals(3, movieRepository
                .findAll(where(filterByMovieType(null))).size());

        assertEquals(1, movieRepository
                .findAll(where(filterByMovieType(MovieType.NEW))).size());
        assertEquals(1, movieRepository
                .findAll(where(filterByMovieType(MovieType.REGULAR))).size());
        assertEquals(1, movieRepository
                .findAll(where(filterByMovieType(MovieType.OLD))).size());

        assertEquals(MovieType.NEW, movieRepository
                .findAll(where(filterByMovieType(MovieType.NEW))).get(0).getType());
        assertEquals(MovieType.REGULAR, movieRepository
                .findAll(where(filterByMovieType(MovieType.REGULAR))).get(0).getType());
        assertEquals(MovieType.OLD, movieRepository
                .findAll(where(filterByMovieType(MovieType.OLD))).get(0).getType());
    }

    @Test
    void testFilterByRentalType() {
        assertEquals(3, movieRepository
                .findAll(where(filterByRentalType(null))).size());

        assertEquals(2, movieRepository
                .findAll(filterByRentalType(MovieRental.Status.ONGOING)).size());
        assertEquals(1, movieRepository
                .findAll(filterByRentalType(MovieRental.Status.RETURNED)).size());
    }

    private static String repeat(int times) {
        return StringUtils.repeat("*", times);
    }
}