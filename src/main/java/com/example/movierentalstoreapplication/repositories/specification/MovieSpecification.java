package com.example.movierentalstoreapplication.repositories.specification;

import com.example.movierentalstoreapplication.model.movie.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;


public final class MovieSpecification {
    private MovieSpecification() {}

    public static Specification<Movie> isPublishedMovie(Boolean published) {
        if (published == null) {
            return null;
        }

        return (movieRoot, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(movieRoot.get(Movie_.PUBLISHED), published);
    }

    public static Specification<Movie> filterByMovieType(MovieType movieType) {
        if (movieType == null) {
            return null;
        }

        return (movieRoot, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(movieRoot.get(Movie_.TYPE), MovieType.valueOf(movieType.name()));
    }

    public static Specification<Movie> filterByRentalType(MovieRental.Status rentalType) {
        if (rentalType == null) {
            return null;
        }

        return (movieRoot, criteriaQuery, criteriaBuilder) -> {
            Join<Movie, MovieRental> movieMovieRentalJoin = movieRoot.join(Movie_.RENTALS, JoinType.LEFT);

            return criteriaBuilder.equal(movieMovieRentalJoin.get(MovieRental_.STATUS), MovieRental.Status.valueOf(rentalType.name()));
        };
    }
}