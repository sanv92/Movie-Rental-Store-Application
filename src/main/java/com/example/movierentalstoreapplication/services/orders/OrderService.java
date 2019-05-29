package com.example.movierentalstoreapplication.services.orders;

import com.example.movierentalstoreapplication.model.movie.MovieOrder;
import com.example.movierentalstoreapplication.repositories.CustomerRepository;
import com.example.movierentalstoreapplication.repositories.MovieOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Qualifier("orderService")
public class OrderService extends AbstractOrder {

    @Autowired
    public OrderService(
            MovieOrderRepository movieOrderRepository,
            CustomerRepository customerRepository,
            @Qualifier("orderRentalService") OrderRental rentalService
    ) {
        super(movieOrderRepository, customerRepository, rentalService);
    }

    @Transactional
    public MovieOrder createOrder(CreateOrderRentalsRequest createOrderRentalsRequest) {
        return movieOrderRepository.save(
                this.calculateCreateOrder(createOrderRentalsRequest)
        );
    }
}