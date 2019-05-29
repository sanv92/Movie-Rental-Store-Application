package com.example.movierentalstoreapplication.services.orders;

import com.example.movierentalstoreapplication.model.services.MovieRentalService;
import com.example.movierentalstoreapplication.repositories.CustomerRepository;
import com.example.movierentalstoreapplication.repositories.MovieRentalRepository;
import com.example.movierentalstoreapplication.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractOrderRental implements OrderRental {

    static final class MovieAlreadyRentedException extends RuntimeException {
        MovieAlreadyRentedException() {
            super();
        }

        @Override
        public String getMessage() {
            return "Cannot rent movie, already rented movie";
        }
    }

    static final class MovieAlreadyReturnedException extends RuntimeException {
        MovieAlreadyReturnedException() {
            super();
        }

        @Override
        public String getMessage() {
            return "Cannot return movie, already returned movie";
        }
    }

    protected final CustomerRepository customerRepository;
    protected final MovieRepository movieRepository;
    protected final MovieRentalRepository movieRentalRepository;
    protected final MovieRentalService movieRentalService;

    @Autowired
    public AbstractOrderRental(
            CustomerRepository customerRepository,
            MovieRepository movieRepository,
            MovieRentalRepository movieRentalRepository,
            MovieRentalService movieRentalService
    ) {
        this.customerRepository = customerRepository;
        this.movieRepository = movieRepository;
        this.movieRentalRepository = movieRentalRepository;
        this.movieRentalService = movieRentalService;
    }
}