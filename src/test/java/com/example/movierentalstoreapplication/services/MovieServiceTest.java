package com.example.movierentalstoreapplication.services;

import com.example.movierentalstoreapplication.exceptions.ConflictException;
import com.example.movierentalstoreapplication.exceptions.ResourceNotFoundException;
import com.example.movierentalstoreapplication.model.movie.Movie;
import com.example.movierentalstoreapplication.model.movie.MovieType;
import com.example.movierentalstoreapplication.repositories.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    private MovieService movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        this.movieService = new MovieService(movieRepository);
    }

    @Test
    void testFindById() {
        Movie movie = new Movie();
        when(movieRepository.findById(anyLong()))
                .thenReturn(Optional.of(movie));

        assertNotNull(movieService.findById(1L));
        verify(movieRepository).findById(1L);
    }

    @Test
    void throwExceptionWhen_FindByIdAndMovieNotExists() {
        when(movieRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> movieService.findById(1L)
        );
    }

    @Test
    void testCreate() {
        Movie movie = mock(Movie.class);
        when(movieRepository.findByTitle("Title"))
                .thenReturn(null);

        when(movieRepository.save(movie))
                .thenReturn(movie);

        assertNotNull(movieService.save(movie));
    }

    @Test
    void throwExceptionWhen_CreateAndMovieAlreadyExists() {
        Movie movie = createMovie();
        when(movieRepository.findByTitle("Title"))
                .thenReturn(movie);

        assertThrows(ConflictException.class,
                () -> movieService.save(movie)
        );
    }

    @Test
    void testUpdate() {
        Movie movie = new Movie("Title2", "Description2", 1992, MovieType.OLD);
        Movie expectedResult = new Movie("Title", "Description", 1993, MovieType.NEW);
        when(movieRepository.findById(anyLong()))
                .thenReturn(Optional.of(movie));

        Movie result = movieService.save(1L, expectedResult);

        assertEquals(result, expectedResult);
    }

    @Test
    void throwExceptionWhen_MovieNotFound() {
        when(movieRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        when(movieRepository.save(any()))
                .thenReturn(any());

        assertThrows(ResourceNotFoundException.class,
                () -> movieService.save(1L, new Movie())
        );
    }

    private Movie createMovie() {
        return new Movie("Title", "Description", 0, MovieType.NEW);
    }
}