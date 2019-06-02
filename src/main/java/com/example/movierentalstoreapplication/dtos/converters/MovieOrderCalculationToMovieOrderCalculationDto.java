package com.example.movierentalstoreapplication.dtos.converters;

import com.example.movierentalstoreapplication.dtos.MovieOrderCalculationDto;
import com.example.movierentalstoreapplication.model.movie.MovieOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MovieOrderCalculationToMovieOrderCalculationDto implements Converter<MovieOrder, MovieOrderCalculationDto> {

    private MovieRentalToMovieRentalDto movieRentalToMovieRentalDto;

    public MovieOrderCalculationToMovieOrderCalculationDto() {
    }

    @Autowired
    public MovieOrderCalculationToMovieOrderCalculationDto(MovieRentalToMovieRentalDto movieRentalToMovieRentalDto) {
        this.movieRentalToMovieRentalDto = movieRentalToMovieRentalDto;
    }

    @Override
    public MovieOrderCalculationDto convert(MovieOrder movieOrder) {
        return new MovieOrderCalculationDto()
                .setTotalPrice(movieOrder.calculateTotalPrice())
                .setTotalPriceForAllDays(movieOrder.calculateTotalPriceByDays())
                .setTotalDiscount(movieOrder.calculateTotalDiscount())
                .setTotalPoints(movieOrder.calculateTotalPoints())
                .setTotalLateCharge(movieOrder.calculateTotalLateCharge())
                .setTotalPriceWithLateCharge(movieOrder.calculateTotalPriceWithLateCharge())
                .setRentals(
                        movieOrder.getRentals()
                                .stream()
                                .map(movieRentalToMovieRentalDto::convert)
                                .collect(Collectors.toList())
                );
    }
}