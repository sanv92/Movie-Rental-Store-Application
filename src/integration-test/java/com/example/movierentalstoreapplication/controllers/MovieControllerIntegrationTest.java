package com.example.movierentalstoreapplication.controllers;

import com.example.movierentalstoreapplication.dtos.MovieDto;
import com.example.movierentalstoreapplication.model.movie.Movie;
import com.example.movierentalstoreapplication.model.movie.MovieRental;
import com.example.movierentalstoreapplication.model.movie.MovieType;
import com.example.movierentalstoreapplication.services.MovieRequest;
import com.example.movierentalstoreapplication.services.MovieService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.thymeleaf.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MovieControllerIntegrationTest extends MockMvcIntegrationTest {

    @MockBean
    private MovieService movieService;

    @Test
    void testGetAllMovies() throws Exception {
        List<Movie> movies = Arrays.asList(createMovie(), createMovie());
        when(movieService.findAll(any(MovieRequest.class), any(Pageable.class)))
                .thenReturn(movies);

        String result = this.mockMvc.perform(
                get("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(movieService, times(1)).findAll(any(MovieRequest.class), any(Pageable.class));
        verifyNoMoreInteractions(movieService);

        String expectedResult = objectMapper.writeValueAsString(Arrays.asList(createMovieDto(), createMovieDto()));

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetAllMoviesWithParams() throws Exception {
        final MovieRequest movieRequest = new MovieRequest(MovieRental.Status.ONGOING, MovieType.REGULAR);
        final Pageable pageable = PageRequest.of(0, 10, new Sort(Sort.Direction.DESC, "id"));

        this.mockMvc.perform(
                get("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("movie", "regular")
                        .param("rental", "ongoing")
                        .param("size", "10")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        verify(movieService, times(1))
                .findAll(movieRequest, pageable);
    }

    @Test
    void testGetMoviesIn() throws Exception {
        List<Movie> movies = Arrays.asList(createMovie(), createMovie());
        when(movieService.findByIdIn(anyList()))
                .thenReturn(movies);

        String result = this.mockMvc.perform(
                get("/movies/searches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "1,2,3")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(movieService, times(1)).findByIdIn(anyList());
        verifyNoMoreInteractions(movieService);

        String expectedResult = objectMapper.writeValueAsString(Arrays.asList(createMovieDto(), createMovieDto()));

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetMovieById() throws Exception {
        MovieDto movieDto = createMovieDto("Title", "Description", 1992, true, MovieType.NEW.name());
        Movie movie = createMovie("Title", "Description", 1992, true, MovieType.NEW);

        when(movieService.findById(anyLong()))
                .thenReturn(movie);

        String result = mockMvc.perform(
                get("/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(movieService).findById(1L);

        String expectedResult = objectMapper.writeValueAsString(movieDto);

        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Should have no violations, when create movie")
    void testCreateMovie() throws Exception {
        MovieDto movieDto = createMovieDto("Tit", repeat(160), 1992, true, MovieType.NEW.name());
        Movie movie = createMovie("Tit", repeat(160), 1992, true, MovieType.NEW);

        when(movieService.save(any(Movie.class)))
                .thenReturn(movie);

        String result = this.mockMvc.perform(
                post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieDto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(movieService, times(1)).save(movie);
        verifyNoMoreInteractions(movieService);

        String expectedResult = objectMapper.writeValueAsString(movieDto);

        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Should detect all invalid data, when create movie")
    void testCreateMovieValidation() throws Exception {
        MovieDto movieDto = createMovieDto("Ti", repeat(159), null, null, " ");

        this.mockMvc.perform(
                post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieDto))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JSON_ERROR_PATH, hasSize(5)));

        verify(movieService, never()).save(any());
    }

    @Test
    @DisplayName("Should have no violations, when update movie")
    void testUpdateMovie() throws Exception {
        MovieDto movieDto = createMovieDto("Tit", repeat(160), 1992, true, MovieType.NEW.name());
        Movie movie = createMovie("Tit", repeat(160), 1992, true, MovieType.NEW);

        when(movieService.save(anyLong(), any(Movie.class)))
                .thenReturn(movie);

        String result = this.mockMvc.perform(
                put("/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieDto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(movieService, times(1)).save(1L, movie);
        verifyNoMoreInteractions(movieService);

        String expectedResult = objectMapper.writeValueAsString(movieDto);

        assertEquals(expectedResult, result);
    }

    @Test
    void testDeleteMovieById() throws Exception {
        this.mockMvc.perform(
                delete("/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        verify(movieService, times(1)).deleteById(1L);
        verifyNoMoreInteractions(movieService);
    }

    private static Movie createMovie() {
        return new Movie("Title", "Description", 1992, MovieType.NEW);
    }

    private static Movie createMovie(String title, String description, Integer year, Boolean published, MovieType movieType) {
        return new Movie(title, description, year, movieType, published);
    }

    private static MovieDto createMovieDto() {
        return new MovieDto()
                .setTitle("Title")
                .setDescription("Description")
                .setYear(1992)
                .setPublished(true)
                .setPrice(MovieType.NEW.calculatePrice())
                .setPoints(MovieType.NEW.calculatePoints())
                .setType(MovieType.NEW.name());
    }

    private static MovieDto createMovieDto(String title, String description, Integer year, Boolean published, String movieType) {
        return new MovieDto()
                .setTitle(title)
                .setDescription(description)
                .setYear(year)
                .setPublished(published)
                .setPrice(MovieType.NEW.calculatePrice())
                .setPoints(MovieType.NEW.calculatePoints())
                .setType(movieType);
    }

    private static String repeat(int times) {
        return StringUtils.repeat("*", times);
    }
}