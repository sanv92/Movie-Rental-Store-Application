package com.example.movierentalstoreapplication.repositories;

import com.example.movierentalstoreapplication.model.movie.MovieRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRentalRepository extends JpaRepository<MovieRental, Long> {
    Optional<MovieRental> findByIdAndStatus(Long id, MovieRental.Status status);

    Optional<MovieRental> findByMovieIdAndStatus(Long id, MovieRental.Status status);
}
