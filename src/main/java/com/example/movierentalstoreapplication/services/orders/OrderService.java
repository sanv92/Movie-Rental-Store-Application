package com.example.movierentalstoreapplication.services.orders;

import com.example.movierentalstoreapplication.exceptions.ResourceNotFoundException;
import com.example.movierentalstoreapplication.model.Customer;
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
        Customer customer = customerRepository.findById(createOrderRentalsRequest.getCustomerId())
                .orElseThrow(ResourceNotFoundException::new);

        MovieOrder movieOrder = new MovieOrder(customer, MovieOrder.Status.OPENED);

        return movieOrderRepository.save(
                this.calculateOrder(movieOrder, createOrderRentalsRequest)
        );
    }
}