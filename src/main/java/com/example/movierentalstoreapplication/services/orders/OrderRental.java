package com.example.movierentalstoreapplication.services.orders;

import com.example.movierentalstoreapplication.model.movie.MovieOrder;
import com.example.movierentalstoreapplication.model.movie.MovieRental;

public interface OrderRental {
    MovieRental createRental(CreateOrderRentalsRequest.Rental rental, MovieOrder movieOrder);

    MovieRental returnRental(Long rentalId);
}