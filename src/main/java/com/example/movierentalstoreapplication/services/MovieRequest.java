package com.example.movierentalstoreapplication.services;

import com.example.movierentalstoreapplication.model.movie.MovieRental;
import com.example.movierentalstoreapplication.model.movie.MovieType;

import java.util.Objects;

public class MovieRequest {
    private MovieRental.Status rental;

    private MovieType movie;

    public MovieRequest() {
    }

    public MovieRequest(MovieRental.Status rental, MovieType movie) {
        this.rental = rental;
        this.movie = movie;
    }

    public MovieRental.Status getRental() {
        return rental;
    }

    public MovieType getMovie() {
        return movie;
    }

    public void setRental(MovieRental.Status rental) {
        this.rental = rental;
    }

    public void setMovie(MovieType movie) {
        this.movie = movie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieRequest)) return false;
        MovieRequest that = (MovieRequest) o;
        return rental == that.rental &&
                movie == that.movie;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rental, movie);
    }

    @Override
    public String toString() {
        return "MovieRequest{" +
                "rental=" + rental +
                ", movie=" + movie +
                '}';
    }
}