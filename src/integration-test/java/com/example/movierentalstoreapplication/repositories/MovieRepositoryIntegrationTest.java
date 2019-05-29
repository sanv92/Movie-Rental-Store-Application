package com.example.movierentalstoreapplication.repositories;

import com.example.movierentalstoreapplication.model.movie.Movie;
import com.example.movierentalstoreapplication.model.movie.MovieType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thymeleaf.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class MovieRepositoryIntegrationTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void testFindByIdIn() {
        Movie movie1 = new Movie(
                "Title1",
                repeat(160),
                1992,
                MovieType.NEW,
                true
        );
        Movie movie2 = new Movie(
                "Title2",
                repeat(160),
                1992, MovieType.NEW,
                false
        );

        movieRepository.saveAll(
                Arrays.asList(movie1, movie2)
        );

        List<Movie> found = movieRepository
                .findByIdIn(Arrays.asList(movie1.getId(), movie2.getId()));

        assertThat(found).isEqualTo(Arrays.asList(movie1, movie2));
    }


    @Test
    public void testFindByTitle() {
        Movie movie = new Movie(
                "Title",
                repeat(160),
                1992,
                MovieType.NEW,
                true
        );

        Movie savedRental = movieRepository.save(movie);

        Movie found = movieRepository
                .findByTitle("Title");

        assertThat(found).isEqualTo(savedRental);
    }

    private static String repeat(int times) {
        return StringUtils.repeat("*", times);
    }
}