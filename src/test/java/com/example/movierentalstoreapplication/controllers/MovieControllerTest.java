package com.example.movierentalstoreapplication.controllers;

import com.example.movierentalstoreapplication.dtos.MovieDto;
import com.example.movierentalstoreapplication.model.movie.Movie;
import com.example.movierentalstoreapplication.model.movie.MovieRental;
import com.example.movierentalstoreapplication.model.movie.MovieType;
import com.example.movierentalstoreapplication.services.MovieRequest;
import com.example.movierentalstoreapplication.services.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @Mock
    private ConversionService conversionService;

    private MovieController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        this.controller = new MovieController(movieService, conversionService);
    }

    @Test
    void testGetAllMovies() {
        final MovieRequest movieRequest = new MovieRequest(MovieRental.Status.ONGOING, MovieType.NEW);
        final Pageable pageable = PageRequest.of(0, 10, new Sort(Sort.Direction.DESC, "id"));

        List<Movie> movies = Arrays.asList(createMovie(), createMovie());
        when(movieService.findAll(any(MovieRequest.class), any(Pageable.class)))
                .thenReturn(movies);

        when(conversionService.convert(any(Movie.class), any()))
                .thenReturn(createMovieDto());

        List<MovieDto> result = controller.getAllMovies(movieRequest, pageable);
        List<MovieDto> expectedResult = Arrays.asList(createMovieDto(), createMovieDto());

        assertArrayEquals(expectedResult.toArray(), result.toArray());
        verify(movieService, times(1)).findAll(movieRequest, pageable);
        verifyNoMoreInteractions(movieService);
    }

    @Test
    void testGetMovieById() {
        when(movieService.findById(anyLong()))
                .thenReturn(createMovie());

        when(conversionService.convert(any(Movie.class), any()))
                .thenReturn(createMovieDto());

        MovieDto result = controller.getMovieById(1L);
        MovieDto expectedResult = createMovieDto();

        assertEquals(expectedResult, result);
        verify(movieService, times(1)).findById(1L);
        verifyNoMoreInteractions(movieService);
    }

    @Test
    void testCreateMovie() {
        when(conversionService.convert(any(MovieDto.class), any()))
                .thenReturn(createMovie());

        when(movieService.save(any(Movie.class)))
                .thenReturn(createMovie());

        when(conversionService.convert(any(Movie.class), any()))
                .thenReturn(createMovieDto());

        MovieDto result = controller.createMovie(createMovieDto());
        MovieDto expectedResult = createMovieDto();

        assertEquals(expectedResult, result);
        verify(movieService, times(1)).save(createMovie());
        verifyNoMoreInteractions(movieService);
    }

    @Test
    void testUpdateMovie() {
        when(conversionService.convert(any(MovieDto.class), any()))
                .thenReturn(createMovie());

        when(movieService.save(anyLong(), any(Movie.class)))
                .thenReturn(createMovie());

        when(conversionService.convert(any(Movie.class), any()))
                .thenReturn(createMovieDto());

        MovieDto result = controller.updateMovie(1L, createMovieDto());
        MovieDto expectedResult = createMovieDto();

        assertEquals(expectedResult, result);
        verify(movieService, times(1)).save(1L, createMovie());
        verifyNoMoreInteractions(movieService);
    }

    @Test
    void testDeleteMovieById() {
        doNothing().when(movieService)
                .deleteById(anyLong());

        controller.deleteMovieById(1L);

        verify(movieService, times(1)).deleteById(1L);
        verifyNoMoreInteractions(movieService);
    }

    private Movie createMovie() {
        return new Movie("Title", "Description", 1992, MovieType.NEW);
    }

    private MovieDto createMovieDto() {
        return new MovieDto()
                .setTitle("Title")
                .setDescription("Description")
                .setType(MovieType.NEW.name())
                .setYear(1992);
    }
}