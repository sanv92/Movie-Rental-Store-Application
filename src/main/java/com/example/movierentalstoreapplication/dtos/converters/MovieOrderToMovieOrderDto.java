package com.example.movierentalstoreapplication.dtos.converters;

import com.example.movierentalstoreapplication.dtos.MovieOrderDto;
import com.example.movierentalstoreapplication.model.movie.MovieOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MovieOrderToMovieOrderDto implements Converter<MovieOrder, MovieOrderDto> {

    private MovieRentalToMovieRentalDto movieRentalToMovieRentalDto;

    public MovieOrderToMovieOrderDto() {
    }

    @Autowired
    public MovieOrderToMovieOrderDto(MovieRentalToMovieRentalDto movieRentalToMovieRentalDto) {
        this.movieRentalToMovieRentalDto = movieRentalToMovieRentalDto;
    }

    @Override
    public MovieOrderDto convert(MovieOrder movieOrder) {
        return new MovieOrderDto()
                .setId(movieOrder.getId())
                .setStatus(movieOrder.getStatus().toString())
                .setCustomerId(movieOrder.getCustomer().getId())
                .setTotalPrice(movieOrder.calculateTotalPrice())
                .setTotalPriceForAllDay(movieOrder.calculateTotalPriceByDays())
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