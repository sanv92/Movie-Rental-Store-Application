package com.example.movierentalstoreapplication.services.orders;

import com.example.movierentalstoreapplication.exceptions.ResourceNotFoundException;
import com.example.movierentalstoreapplication.model.Customer;
import com.example.movierentalstoreapplication.model.movie.MovieOrder;
import com.example.movierentalstoreapplication.model.movie.MovieRental;
import com.example.movierentalstoreapplication.repositories.CustomerRepository;
import com.example.movierentalstoreapplication.repositories.MovieOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private MovieOrderRepository movieOrderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRentalService rentalService;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.orderService = new OrderService(movieOrderRepository, customerRepository, rentalService);
    }

    @Test
    void testFindById() {
        MovieOrder movieOrder = new MovieOrder();
        when(movieOrderRepository.findById(anyLong()))
                .thenReturn(Optional.of(movieOrder));

        assertEquals(movieOrder, orderService.findById(1L));
        verify(movieOrderRepository).findById(1L);
    }

    @Test
    void throwExceptionWhen_FindByIdAndOrderNotExists() {
        when(movieOrderRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.findById(1L)
        );

        verify(movieOrderRepository).findById(1L);
    }


    @Nested
    @DisplayName("Create Order")
    class CreateOrder {
        @Test
        void createOrder() {
            CreateOrderRentalsRequest createOrderRentalsRequest = createOrderRentalsRequest();

            Customer customer = new Customer();
            when(customerRepository.findById(anyLong()))
                    .thenReturn(Optional.of(customer));

            MovieOrder movieOrder = new MovieOrder(customer, MovieOrder.Status.OPENED);
            when(movieOrderRepository.save(any()))
                    .thenReturn(movieOrder);

            assertEquals(movieOrder, orderService.createOrder(createOrderRentalsRequest));
            verify(customerRepository).findById(createOrderRentalsRequest.getCustomerId());
            verify(movieOrderRepository).save(movieOrder);
        }

        @Test
        void throwExceptionWhen_CustomerNotFound() {
            CreateOrderRentalsRequest createOrderRentalsRequest = createOrderRentalsRequest();

            when(customerRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            MovieOrder movieOrder = new MovieOrder();
            when(movieOrderRepository.save(any()))
                    .thenReturn(movieOrder);


            assertThrows(ResourceNotFoundException.class,
                    () -> orderService.createOrder(createOrderRentalsRequest)
            );

            verify(customerRepository).findById(createOrderRentalsRequest.getCustomerId());
            verify(movieOrderRepository, never()).save(any());
        }

        private CreateOrderRentalsRequest createOrderRentalsRequest() {
            return new CreateOrderRentalsRequest(
                    1L,
                    Arrays.asList(
                            new CreateOrderRentalsRequest.Rental(1L, 1, 0),
                            new CreateOrderRentalsRequest.Rental(2L, 2, 0)
                    ));
        }
    }

    @Nested
    @DisplayName("Return All Order Rentals")
    class returnRental {
        @Test
        void returnOrderRentals() {
            MovieOrder movieOrder = new MovieOrder()
                    .setStatus(MovieOrder.Status.OPENED)
                    .setRentals(
                            Arrays.asList(
                                    new MovieRental()
                                            .setStatus(MovieRental.Status.ONGOING),
                                    new MovieRental()
                                            .setStatus(MovieRental.Status.RETURNED)
                            )
                    );

            when(movieOrderRepository.findById(anyLong()))
                    .thenReturn(Optional.of(movieOrder));

            when(movieOrderRepository.save(any()))
                    .thenReturn(movieOrder);

            MovieOrder order = orderService
                    .returnOrderRentals(1L);

            assertNotNull(order);
            assertEquals(1, order.getRentals().size());
            verify(movieOrderRepository).findById(1L);
        }

        @Test
        void throwExceptionWhen_MovieOrderNotFound() {
            when(movieOrderRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> orderService.returnOrderRentals(1L)
            );

            verify(movieOrderRepository).findById(1L);
        }

        @Test
        void throwExceptionWhen_OrderAlreadyClosed() {
            when(movieOrderRepository.findById(anyLong()))
                    .thenReturn(Optional.of(
                            new MovieOrder()
                                    .setStatus(MovieOrder.Status.CLOSED)
                    ));

            assertThrows(OrderService.OrderAlreadyClosedException.class,
                    () -> orderService.returnOrderRentals(1L)
            );

            verify(movieOrderRepository).findById(1L);
        }
    }
}