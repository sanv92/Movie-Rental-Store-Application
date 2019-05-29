package com.example.movierentalstoreapplication.services.orders;

import com.example.movierentalstoreapplication.exceptions.ResourceNotFoundException;
import com.example.movierentalstoreapplication.model.Customer;
import com.example.movierentalstoreapplication.model.movie.Movie;
import com.example.movierentalstoreapplication.model.movie.MovieOrder;
import com.example.movierentalstoreapplication.model.movie.MovieRental;
import com.example.movierentalstoreapplication.model.services.MovieRentalService;
import com.example.movierentalstoreapplication.repositories.CustomerRepository;
import com.example.movierentalstoreapplication.repositories.MovieRentalRepository;
import com.example.movierentalstoreapplication.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Qualifier("orderRentalService")
public class OrderRentalService extends AbstractOrderRental {

    public OrderRentalService(CustomerRepository customerRepository, MovieRepository movieRepository, MovieRentalRepository movieRentalRepository, MovieRentalService movieRentalService) {
        super(customerRepository, movieRepository, movieRentalRepository, movieRentalService);
    }

    public MovieRental createRental(CreateOrderRentalsRequest.Rental rental, MovieOrder movieOrder) {
        Movie movie = movieRepository.findById(rental.getMovieId())
                .orElseThrow(ResourceNotFoundException::new);

        if (movieRentalRepository
                .findByMovieIdAndStatus(movie.getId(), MovieRental.Status.ONGOING).isPresent()) {
            throw new MovieAlreadyRentedException();
        }

        Customer customer = customerRepository.findById(movieOrder.getCustomer().getId())
                .orElseThrow(ResourceNotFoundException::new);

        return movieRentalService.rentMovie(
                customer,
                new MovieRental(movieOrder, movie, rental.getNumberOfDays(), rental.getBonusPoints())
        );
    }

    @Transactional
    public MovieRental returnRental(Long rentalId) {
        MovieRental movieRental = movieRentalRepository.findById(rentalId)
                .orElseThrow(ResourceNotFoundException::new);

        if (movieRentalRepository
                .findByIdAndStatus(movieRental.getId(), MovieRental.Status.RETURNED).isPresent()) {
            throw new MovieAlreadyReturnedException();
        }

        return movieRentalRepository.save(
                movieRentalService.returnMovie(
                        movieRental.getOrder().getCustomer(),
                        movieRental
                )
        );
    }
}