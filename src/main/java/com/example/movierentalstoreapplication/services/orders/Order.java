package com.example.movierentalstoreapplication.services.orders;

import com.example.movierentalstoreapplication.model.movie.MovieOrder;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Order {
    List<MovieOrder> findAll(Pageable pageable);

    MovieOrder findById(Long orderId);

    MovieOrder createOrder(CreateOrderRentalsRequest createOrderRentalsRequest);

    MovieOrder returnOrderRentals(Long orderId);
}
