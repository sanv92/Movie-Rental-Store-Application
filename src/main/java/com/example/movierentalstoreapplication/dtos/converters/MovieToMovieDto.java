package com.example.movierentalstoreapplication.dtos.converters;

import com.example.movierentalstoreapplication.dtos.MovieDto;
import com.example.movierentalstoreapplication.model.movie.Movie;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MovieToMovieDto implements Converter<Movie, MovieDto> {

    @Override
    public MovieDto convert(Movie movie) {
        return new MovieDto()
                .setId(movie.getId())
                .setTitle(movie.getTitle())
                .setDescription(movie.getDescription())
                .setYear(movie.getYear())
                .setType(movie.getType().name())
                .setPrice(movie.getType().calculatePrice())
                .setPoints(movie.getType().calculatePoints())
                .setPublished(movie.isPublished());
    }
}
