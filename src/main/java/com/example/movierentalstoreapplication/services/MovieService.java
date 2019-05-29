package com.example.movierentalstoreapplication.services;

import com.example.movierentalstoreapplication.exceptions.ConflictException;
import com.example.movierentalstoreapplication.exceptions.ResourceNotFoundException;
import com.example.movierentalstoreapplication.model.movie.Movie;
import com.example.movierentalstoreapplication.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.movierentalstoreapplication.repositories.specification.MovieSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional(readOnly = true)
    public List<Movie> findAll(MovieRequest movieRequest, Pageable pageable) {
        return movieRepository.findAll(
                where(isPublishedMovie(true))
                        .and(filterByMovieType(movieRequest.getMovie()))
                        .and(filterByRentalType(movieRequest.getRental()))
                , pageable).getContent();
    }

    @Transactional(readOnly = true)
    public List<Movie> findByIdIn(List<Long> movieIds) {
        return movieRepository.findByIdIn(movieIds);
    }

    @Transactional(readOnly = true)
    public Movie findById(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public Movie save(Movie movie) {
        if (Optional.ofNullable(movieRepository.findByTitle(movie.getTitle())).isPresent()) {
            throw new ConflictException();
        }

        return movieRepository.save(movie);
    }

    @Transactional
    public Movie save(Long movieId, Movie movie) {
        return movieRepository.findById(movieId).orElseThrow(ResourceNotFoundException::new)
                .setTitle(movie.getTitle())
                .setDescription(movie.getDescription())
                .setYear(movie.getYear())
                .setYear(movie.getYear())
                .setPublished(movie.isPublished());
    }

    @Transactional
    public void deleteById(Long movieId) {
        movieRepository.deleteById(movieId);
    }
}