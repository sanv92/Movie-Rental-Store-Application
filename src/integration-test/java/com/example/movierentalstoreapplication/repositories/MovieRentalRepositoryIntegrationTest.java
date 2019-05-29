package com.example.movierentalstoreapplication.repositories;

import com.example.movierentalstoreapplication.model.Customer;
import com.example.movierentalstoreapplication.model.movie.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thymeleaf.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class MovieRentalRepositoryIntegrationTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MovieRentalRepository movieRentalRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdAndStatus() {
        Customer customer = customerRepository.save(
                new Customer("First name", "Last name", 0, 0)
        );

        MovieOrder movieOrder = new MovieOrder(customer, MovieOrder.Status.OPENED);

        Movie movie = movieRepository.save(
                new Movie(
                        "Title1",
                        repeat(160),
                        1992,
                        MovieType.NEW,
                        true
                )
        );

        MovieRental movieRental = new MovieRental(
                movieOrder,
                movie,
                2,
                0
        )
                .setStatus(MovieRental.Status.ONGOING)
                .setPriceType(PriceType.PREMIUM);

        movieOrder
                .setRentals(Arrays.asList(movieRental));

        MovieRental savedRental = movieRentalRepository.save(movieRental);

        Optional<MovieRental> found = movieRentalRepository
                .findByIdAndStatus(savedRental.getId(), MovieRental.Status.ONGOING);

        assertThat(found.get()).isEqualTo(savedRental);
    }

    private static String repeat(int times) {
        return StringUtils.repeat("*", times);
    }
}