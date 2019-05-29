package com.example.movierentalstoreapplication.dtos.converters;

import com.example.movierentalstoreapplication.dtos.MovieRentalDto;
import com.example.movierentalstoreapplication.model.movie.MovieRental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MovieRentalToMovieRentalDto implements Converter<MovieRental, MovieRentalDto> {

    private MovieToMovieDto movieToMovieDto;

    public MovieRentalToMovieRentalDto() {
    }

    @Autowired
    public MovieRentalToMovieRentalDto(MovieToMovieDto movieToMovieDto) {
        this.movieToMovieDto = movieToMovieDto;
    }

    @Override
    public MovieRentalDto convert(MovieRental movieRental) {
        return new MovieRentalDto()
                .setId(movieRental.getId())
                .setMovie(movieToMovieDto.convert(movieRental.getMovie()))
                .setStatus(movieRental.getStatus().name())
                .setPriceType(movieRental.getPriceType().name())
                .setPrice(movieRental.getPrice())
                .setPriceForOneDay(movieRental.calculatePrice())
                .setPriceForAllDay(movieRental.calculatePriceByDays())
                .setDiscount(movieRental.calculateDiscount())
                .setLateCharge(movieRental.getLateCharge())
                .setPoints(movieRental.getPoints())
                .setNumberOfDays(movieRental.getNumberOfDays())
                .setExtraDays(movieRental.calculateExtraDays())
                .setPickupDate(movieRental.getPickupDate())
                .setReturnDate(movieRental.getReturnDate());
    }
}