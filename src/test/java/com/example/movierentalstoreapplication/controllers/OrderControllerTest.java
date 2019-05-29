package com.example.movierentalstoreapplication.controllers;

import com.example.movierentalstoreapplication.dtos.MovieOrderDto;
import com.example.movierentalstoreapplication.model.Customer;
import com.example.movierentalstoreapplication.model.movie.MovieOrder;
import com.example.movierentalstoreapplication.services.orders.CreateOrderRentalsRequest;
import com.example.movierentalstoreapplication.services.orders.OrderCalculationService;
import com.example.movierentalstoreapplication.services.orders.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private OrderCalculationService orderCalculation;

    @Mock
    private ConversionService conversionService;

    private OrderController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        this.controller = new OrderController(orderService, orderCalculation, conversionService);
    }

    @Test
    void testGetAllOrders() {
        final Pageable pageable = PageRequest.of(0, 10, new Sort(Sort.Direction.DESC, "id"));

        List<MovieOrder> movies = Arrays.asList(createMovieOrder(), createMovieOrder());
        when(orderService.findAll(any(Pageable.class)))
                .thenReturn(movies);

        when(conversionService.convert(any(MovieOrder.class), any()))
                .thenReturn(createMovieOrderDto());

        List<MovieOrderDto> result = controller.getAllOrders(pageable);
        List<MovieOrderDto> expectedResult = Arrays.asList(createMovieOrderDto(), createMovieOrderDto());

        assertArrayEquals(expectedResult.toArray(), result.toArray());
        verify(orderService, times(1)).findAll(pageable);
        verifyNoMoreInteractions(orderService);
    }

    @Test
    void testGetOrderById() {
        when(orderService.findById(anyLong()))
                .thenReturn(createMovieOrder());

        when(conversionService.convert(any(MovieOrder.class), any()))
                .thenReturn(createMovieOrderDto());

        MovieOrderDto result = controller.getOrderById(1L);
        MovieOrderDto expectedResult = createMovieOrderDto();

        assertEquals(expectedResult, result);
        verify(orderService, times(1)).findById(1L);
        verifyNoMoreInteractions(orderService);
    }

    @Test
    void testCreateOrder() {
        final CreateOrderRentalsRequest createOrderRentalsRequest = new CreateOrderRentalsRequest(
                1L,
                Arrays.asList(
                        new CreateOrderRentalsRequest.Rental(1L, 1, 0),
                        new CreateOrderRentalsRequest.Rental(2L, 2, 1)
                )
        );

        when(orderService.createOrder(any(CreateOrderRentalsRequest.class)))
                .thenReturn(createMovieOrder());

        when(conversionService.convert(any(MovieOrder.class), any()))
                .thenReturn(createMovieOrderDto());

        MovieOrderDto result = controller.createOrder(createOrderRentalsRequest);
        MovieOrderDto expectedResult = createMovieOrderDto();

        assertEquals(expectedResult, result);
        verify(orderService, times(1)).createOrder(createOrderRentalsRequest);
        verifyNoMoreInteractions(orderService);
    }

    @Test
    void testReturnOrderRentals() {
        when(orderService.returnOrderRentals(anyLong()))
                .thenReturn(createMovieOrder());

        when(conversionService.convert(any(MovieOrder.class), any()))
                .thenReturn(createMovieOrderDto());

        MovieOrderDto result = controller.returnOrderRentals(1L);
        MovieOrderDto expectedResult = createMovieOrderDto();

        assertEquals(expectedResult, result);
        verify(orderService, times(1)).returnOrderRentals(1L);
        verifyNoMoreInteractions(orderService);
    }

    private MovieOrder createMovieOrder() {
        Customer customer = new Customer("First Name", "Last Name", 1992, 0);

        return new MovieOrder(customer, MovieOrder.Status.OPENED);
    }

    private MovieOrderDto createMovieOrderDto() {
        return new MovieOrderDto()
                .setCustomerId(1L)
                .setStatus(MovieOrder.Status.OPENED.name());
    }
}