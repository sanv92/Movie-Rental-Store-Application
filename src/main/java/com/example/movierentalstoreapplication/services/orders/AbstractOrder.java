package com.example.movierentalstoreapplication.services.orders;

import com.example.movierentalstoreapplication.exceptions.ResourceNotFoundException;
import com.example.movierentalstoreapplication.model.Customer;
import com.example.movierentalstoreapplication.model.movie.MovieOrder;
import com.example.movierentalstoreapplication.model.movie.MovieRental;
import com.example.movierentalstoreapplication.repositories.CustomerRepository;
import com.example.movierentalstoreapplication.repositories.MovieOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractOrder implements Order {

    static final class OrderAlreadyClosedException extends RuntimeException {
        OrderAlreadyClosedException() {
            super();
        }

        @Override
        public String getMessage() {
            return "Cannot close order, already closed order";
        }
    }

    protected final MovieOrderRepository movieOrderRepository;
    protected final CustomerRepository customerRepository;
    private final OrderRental rentalService;

    @Autowired
    public AbstractOrder(
            MovieOrderRepository movieOrderRepository,
            CustomerRepository customerRepository,
            @Qualifier("orderRentalService") OrderRental rentalService
    ) {
        this.movieOrderRepository = movieOrderRepository;
        this.customerRepository = customerRepository;
        this.rentalService = rentalService;
    }

    @Transactional(readOnly = true)
    public List<MovieOrder> findAll(Pageable pageable) {
        return movieOrderRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    public MovieOrder findById(Long orderId) {
        return movieOrderRepository.findById(orderId)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public MovieOrder calculateCreateOrder(CreateOrderRentalsRequest createOrderRentalsRequest) {
        Customer customer = customerRepository.findById(createOrderRentalsRequest.getCustomerId())
                .orElseThrow(ResourceNotFoundException::new);

        MovieOrder movieOrder = new MovieOrder(customer, MovieOrder.Status.OPENED);
        List<MovieRental> movieRentals = createOrderRentalsRequest.getRentals()
                .stream()
                .map(rental -> rentalService.createRental(
                        new CreateOrderRentalsRequest.Rental(
                                rental.getMovieId(),
                                rental.getNumberOfDays(),
                                rental.getBonusPoints()
                        ),
                        movieOrder
                ))
                .collect(Collectors.toList());

        return movieOrder
                .setRentals(movieRentals);
    }

    @Transactional
    public MovieOrder returnOrderRentals(Long orderId) {
        MovieOrder movieOrder = movieOrderRepository.findById(orderId)
                .orElseThrow(ResourceNotFoundException::new);

        if (movieOrder.isClosed()) {
            throw new OrderAlreadyClosedException();
        }

        List<MovieRental> movieRentals = movieOrder.getRentals()
                .stream()
                .filter(rental -> rental.getStatus().equals(MovieRental.Status.ONGOING))
                .map(rental -> rentalService.returnRental(rental.getId()))
                .collect(Collectors.toList());

        return movieOrder
                .setRentals(movieRentals);
    }
}