package com.example.movierentalstoreapplication.services.orders;

import com.example.movierentalstoreapplication.exceptions.ResourceNotFoundException;
import com.example.movierentalstoreapplication.model.Customer;
import com.example.movierentalstoreapplication.model.movie.*;
import com.example.movierentalstoreapplication.model.services.MovieRentalService;
import com.example.movierentalstoreapplication.repositories.CustomerRepository;
import com.example.movierentalstoreapplication.repositories.MovieRentalRepository;
import com.example.movierentalstoreapplication.repositories.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderRentalServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieRentalRepository movieRentalRepository;

    @Mock
    private MovieRentalService movieRentalService;

    private OrderRentalService orderRentalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        this.orderRentalService = new OrderRentalService(
                customerRepository,
                movieRepository,
                movieRentalRepository,
                movieRentalService
        );
    }

    @Nested
    @DisplayName("Create Rental")
    class CreateRental {
        @Test
        void createRental() {
            Movie movie = new Movie();
            when(movieRepository.findById(anyLong()))
                    .thenReturn(Optional.of(movie));

            when(movieRentalRepository.findByMovieIdAndStatus(anyLong(), any()))
                    .thenReturn(Optional.empty());

            MovieOrder movieOrder = mock(MovieOrder.class);
            Customer customer = mock(Customer.class);
            when(movieOrder.getCustomer()).thenReturn(customer);
            when(customerRepository.findById(anyLong()))
                    .thenReturn(Optional.of(new Customer()));

            when(movieRentalService.rentMovie(any(Customer.class), any(MovieRental.class)))
                    .thenReturn(new MovieRental());

            MovieRental result = orderRentalService.createRental(
                    new CreateOrderRentalsRequest.Rental(1L, 1, 0),
                    movieOrder
            );

            assertNotNull(result);
        }

        @Test
        void throwExceptionWhen_MovieNotFound() {
            when(movieRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> orderRentalService
                            .createRental(
                                    new CreateOrderRentalsRequest.Rental(1L, 1, 0),
                                    new MovieOrder()
                            )
            );

            verify(movieRepository).findById(anyLong());
            verify(movieRentalRepository, never()).findByMovieIdAndStatus(anyLong(), any());
        }

        @Test
        void throwExceptionWhen_MovieAlreadyRented() {
            Movie movie = mock(Movie.class);
            when(movieRepository.findById(anyLong()))
                    .thenReturn(Optional.of(movie));

            when(movieRentalRepository.findByMovieIdAndStatus(anyLong(), any()))
                    .thenReturn(Optional.of(new MovieRental()));

            assertThrows(OrderRentalService.MovieAlreadyRentedException.class,
                    () -> orderRentalService
                            .createRental(
                                    new CreateOrderRentalsRequest.Rental(1L, 1, 0),
                                    new MovieOrder()
                            )
            );

            verify(movieRepository).findById(anyLong());
            verify(movieRentalRepository).findByMovieIdAndStatus(anyLong(), any());
            verify(customerRepository, never()).findById(anyLong());
        }

        @Test
        void throwExceptionWhen_CustomerNotFound() {
            Movie movie = mock(Movie.class);
            when(movieRepository.findById(anyLong()))
                    .thenReturn(Optional.of(movie));

            when(movieRentalRepository.findByMovieIdAndStatus(anyLong(), any()))
                    .thenReturn(Optional.empty());

            MovieOrder movieOrder = mock(MovieOrder.class);
            Customer customer = mock(Customer.class);
            when(movieOrder.getCustomer()).thenReturn(customer);
            when(customerRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> orderRentalService
                            .createRental(
                                    new CreateOrderRentalsRequest.Rental(1L, 1, 0),
                                    movieOrder
                            )
            );

            verify(movieRepository).findById(anyLong());
            verify(movieRentalRepository).findByMovieIdAndStatus(anyLong(), any());
            verify(customerRepository).findById(anyLong());
        }
    }

    @Nested
    @DisplayName("Return Rental")
    class returnRental {
        @Test
        void returnRental() {
            MovieRental movieRental = mock(MovieRental.class);
            when(movieRentalRepository.findById(anyLong()))
                    .thenReturn(Optional.of(movieRental));

            Movie movie = mock(Movie.class);
            when(movieRental.getMovie()).thenReturn(movie);
            when(movieRentalRepository.findByMovieIdAndStatus(anyLong(), any(MovieRental.Status.class)))
                    .thenReturn(Optional.empty());

            MovieOrder movieOrder = mock(MovieOrder.class);
            when(movieRental.getOrder())
                    .thenReturn(movieOrder);
            when(movieRentalRepository.save(any()))
                    .thenReturn(new MovieRental());

            MovieRental result = orderRentalService.returnRental(1L);

            assertNotNull(result);

            verify(movieRentalRepository).findById(1L);
            verify(movieRentalRepository).findByIdAndStatus(anyLong(), any());
            verify(movieRentalRepository).save(any());
        }

        @Test
        void throwExceptionWhen_MovieNotFound() {
            when(movieRentalRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> orderRentalService.returnRental(1L)
            );
            verify(movieRentalRepository, never()).findByMovieIdAndStatus(anyLong(), any());
            verify(movieRentalRepository, never()).save(any());
        }

        @Test
        void throwExceptionWhen_MovieAlreadyReturned() {
            MovieRental movieRental = mock(MovieRental.class);
            when(movieRentalRepository.findById(anyLong()))
                    .thenReturn(Optional.of(movieRental));

            Movie movie = mock(Movie.class);
            when(movieRental.getMovie()).thenReturn(movie);
            when(movieRentalRepository.findByIdAndStatus(anyLong(), any()))
                    .thenReturn(Optional.of(movieRental));

            assertThrows(OrderRentalService.MovieAlreadyReturnedException.class,
                    () -> orderRentalService.returnRental(1L)
            );

            verify(movieRentalRepository).findById(1L);
            verify(movieRentalRepository).findByIdAndStatus(anyLong(), any());
            verify(movieRentalRepository, never()).save(any());
        }
    }
}