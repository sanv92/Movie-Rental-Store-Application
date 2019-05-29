package com.example.movierentalstoreapplication.services.orders;

import com.example.movierentalstoreapplication.exceptions.ResourceNotFoundException;
import com.example.movierentalstoreapplication.model.movie.Movie;
import com.example.movierentalstoreapplication.model.movie.MovieOrder;
import com.example.movierentalstoreapplication.model.movie.MovieRental;
import com.example.movierentalstoreapplication.model.services.MovieRentalService;
import com.example.movierentalstoreapplication.repositories.CustomerRepository;
import com.example.movierentalstoreapplication.repositories.MovieRentalRepository;
import com.example.movierentalstoreapplication.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("orderRentalCalculation")
public class OrderRentalCalculationService extends AbstractOrderRental {

    public OrderRentalCalculationService(CustomerRepository customerRepository, MovieRepository movieRepository, MovieRentalRepository movieRentalRepository, MovieRentalService movieRentalService) {
        super(customerRepository, movieRepository, movieRentalRepository, movieRentalService);
    }

    public MovieRental createRental(CreateOrderRentalsRequest.Rental rental, MovieOrder movieOrder) {
        Movie movie = movieRepository.findById(rental.getMovieId())
                .orElseThrow(ResourceNotFoundException::new);

        return movieRentalService.calculateRentMovie(
                new MovieRental(movieOrder, movie, rental.getNumberOfDays(), rental.getBonusPoints())
        );
    }

    public MovieRental returnRental(Long rentalId) {
        MovieRental movieRental = movieRentalRepository.findById(rentalId)
                .orElseThrow(ResourceNotFoundException::new);

        return movieRentalService.returnMovie(
                movieRental.getOrder().getCustomer(),
                movieRental
        );
    }
}