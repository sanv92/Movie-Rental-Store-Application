package com.example.movierentalstoreapplication.services.orders;

import com.example.movierentalstoreapplication.model.movie.MovieOrder;
import com.example.movierentalstoreapplication.repositories.CustomerRepository;
import com.example.movierentalstoreapplication.repositories.MovieOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Qualifier("orderCalculation")
public class OrderCalculationService extends AbstractOrder {

    @Autowired
    public OrderCalculationService(
            MovieOrderRepository movieOrderRepository,
            CustomerRepository customerRepository,
            @Qualifier("orderRentalCalculationService") OrderRental rentalService
    ) {
        super(movieOrderRepository, customerRepository, rentalService);
    }

    @Transactional
    public MovieOrder createOrder(CreateOrderRentalsRequest createOrderRentalsRequest) {
        return this.calculateOrder(
                new MovieOrder(),
                createOrderRentalsRequest
        );
    }
}