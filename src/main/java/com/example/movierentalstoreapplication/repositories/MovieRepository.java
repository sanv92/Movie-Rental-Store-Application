package com.example.movierentalstoreapplication.repositories;

import com.example.movierentalstoreapplication.model.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    Movie findByTitle(String title);

    List<Movie> findByIdIn(List<Long> movieId);
}