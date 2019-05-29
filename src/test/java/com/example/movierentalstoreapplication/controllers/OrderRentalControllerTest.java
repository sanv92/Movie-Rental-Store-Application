package com.example.movierentalstoreapplication.controllers;

import com.example.movierentalstoreapplication.dtos.MovieRentalDto;
import com.example.movierentalstoreapplication.model.Customer;
import com.example.movierentalstoreapplication.model.movie.MovieOrder;
import com.example.movierentalstoreapplication.model.movie.MovieRental;
import com.example.movierentalstoreapplication.services.orders.OrderRentalService;
import com.example.movierentalstoreapplication.services.orders.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderRentalControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private ConversionService conversionService;

    @Mock
    private OrderRentalService orderRentalService;

    private OrderRentalController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        this.controller = new OrderRentalController(orderService, orderRentalService, conversionService);
    }

    @Test
    void getAllRentals() {
        when(orderService.findById(anyLong()))
                .thenReturn(createMovieOrderWithRentals());

        when(conversionService.convert(any(MovieRental.class), any()))
                .thenReturn(createMovieRentalDto());

        List<MovieRentalDto> result = controller.getAllRentals(1L);
        List<MovieRentalDto> expectedResult = Arrays.asList(createMovieRentalDto(), createMovieRentalDto());

        assertArrayEquals(expectedResult.toArray(), result.toArray());
        verify(orderService, times(1)).findById(1L);
        verifyNoMoreInteractions(orderService);
    }

    private MovieRentalDto createMovieRentalDto() {
        return new MovieRentalDto();
    }

    private MovieOrder createMovieOrderWithRentals() {
        Customer customer = new Customer("First Name", "Last Name", 1992, 0);
        List<MovieRental> movieRentals = Arrays.asList(new MovieRental(), new MovieRental());

        return new MovieOrder(customer, MovieOrder.Status.OPENED, movieRentals);
    }
}