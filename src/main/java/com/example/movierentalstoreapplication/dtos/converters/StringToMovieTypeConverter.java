package com.example.movierentalstoreapplication.dtos.converters;

import com.example.movierentalstoreapplication.model.movie.MovieType;
import org.springframework.core.convert.converter.Converter;

public class StringToMovieTypeConverter implements Converter<String, MovieType> {
    @Override
    public MovieType convert(String movieType) {
        for (MovieType value : MovieType.values()) {
            String name = movieType.trim().toUpperCase();
            if (value.name().equals(name))
                return MovieType.valueOf(name);
        }

        return null;
    }
}