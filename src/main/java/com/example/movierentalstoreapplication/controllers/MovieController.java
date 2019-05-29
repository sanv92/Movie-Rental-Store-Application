package com.example.movierentalstoreapplication.controllers;

import com.example.movierentalstoreapplication.dtos.MovieDto;
import com.example.movierentalstoreapplication.model.movie.Movie;
import com.example.movierentalstoreapplication.services.MovieRequest;
import com.example.movierentalstoreapplication.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("/movies")
@RestController
public class MovieController {

    private final MovieService movieService;

    private final ConversionService conversionService;

    @Autowired
    public MovieController(
            MovieService movieService,
            ConversionService conversionService
    ) {
        this.movieService = movieService;
        this.conversionService = conversionService;
    }

    @GetMapping
    public List<MovieDto> getAllMovies(
            MovieRequest movieRequest,
            @PageableDefault(size = Integer.MAX_VALUE, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return movieService.findAll(movieRequest, pageable)
                .stream()
                .map(movie -> conversionService.convert(movie, MovieDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/searches")
    public List<MovieDto> getMoviesIn(@RequestParam(value = "id", required = false) List<Long> movieIds) {
        return movieService.findByIdIn(movieIds)
                .stream()
                .map(movie -> conversionService.convert(movie, MovieDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{movieId}")
    public MovieDto getMovieById(@PathVariable("movieId") Long movieId) {
        return conversionService.convert(
                movieService.findById(movieId), MovieDto.class
        );
    }

    @PostMapping
    public MovieDto createMovie(@Valid @RequestBody MovieDto movieDto) {
        return conversionService.convert(
                movieService.save(conversionService.convert(movieDto, Movie.class)), MovieDto.class
        );
    }

    @PutMapping("/{movieId}")
    public MovieDto updateMovie(@PathVariable("movieId") Long movieId, @Valid @RequestBody MovieDto movieDto) {
        return conversionService.convert(
                movieService.save(movieId, conversionService.convert(movieDto, Movie.class)), MovieDto.class
        );
    }

    @DeleteMapping("/{movieId}")
    public void deleteMovieById(@PathVariable("movieId") Long movieId) {
        movieService.deleteById(movieId);
    }
}
