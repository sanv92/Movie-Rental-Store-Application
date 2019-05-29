package com.example.movierentalstoreapplication.repositories;

import com.example.movierentalstoreapplication.model.movie.MovieOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieOrderRepository extends JpaRepository<MovieOrder, Long> {
}
