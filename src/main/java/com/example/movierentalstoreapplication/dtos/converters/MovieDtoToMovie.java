package com.example.movierentalstoreapplication.dtos.converters;

import com.example.movierentalstoreapplication.dtos.MovieDto;
import com.example.movierentalstoreapplication.model.movie.Movie;
import com.example.movierentalstoreapplication.model.movie.MovieType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MovieDtoToMovie implements Converter<MovieDto, Movie> {

    @Override
    public Movie convert(MovieDto movieDto) {
        return new Movie(
                movieDto.getTitle(),
                movieDto.getDescription(),
                movieDto.getYear(),
                MovieType.valueOf(movieDto.getType()),
                movieDto.getPublished()
        );
    }
}