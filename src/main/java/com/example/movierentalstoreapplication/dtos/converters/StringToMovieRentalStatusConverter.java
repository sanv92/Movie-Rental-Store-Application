package com.example.movierentalstoreapplication.dtos.converters;

import com.example.movierentalstoreapplication.model.movie.MovieRental;
import org.springframework.core.convert.converter.Converter;

public class StringToMovieRentalStatusConverter implements Converter<String, MovieRental.Status> {
    @Override
    public MovieRental.Status convert(String rental) {
        for (MovieRental.Status value : MovieRental.Status.values()) {
            String name = rental.trim().toUpperCase();
            if (value.name().equals(name))
                return MovieRental.Status.valueOf(name);
        }

        return null;
    }
}