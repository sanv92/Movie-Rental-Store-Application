package com.example.movierentalstoreapplication.controllers;

import com.example.movierentalstoreapplication.dtos.MovieOrderDto;
import com.example.movierentalstoreapplication.services.orders.CreateOrderRentalsRequest;
import com.example.movierentalstoreapplication.services.orders.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("/orders")
@RestController
public class OrderController {

    private final Order orderService;
    private final Order orderCalculation;
    private final ConversionService conversionService;

    @Autowired
    public OrderController(
            @Qualifier("orderService") Order orderService,
            @Qualifier("orderCalculationService") Order orderCalculation,
            ConversionService conversionService
    ) {
        this.orderService = orderService;
        this.orderCalculation = orderCalculation;
        this.conversionService = conversionService;
    }

    @GetMapping
    public List<MovieOrderDto> getAllOrders(
            @PageableDefault(size = Integer.MAX_VALUE, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return orderService.findAll(pageable).stream()
                .map(order -> conversionService.convert(order, MovieOrderDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{orderId}")
    public MovieOrderDto getOrderById(@PathVariable("orderId") Long orderId) {
        return conversionService
                .convert(orderService.findById(orderId), MovieOrderDto.class);
    }

    @PostMapping
    public MovieOrderDto createOrder(@Valid @RequestBody CreateOrderRentalsRequest createOrderRentalsRequest) {
        return conversionService.convert(
                orderService.createOrder(createOrderRentalsRequest), MovieOrderDto.class
        );
    }

    @PostMapping("/calculation")
    public MovieOrderDto orderCalculation(@Valid @RequestBody CreateOrderRentalsRequest createOrderRentalsRequest) {
        return conversionService.convert(
                orderCalculation.createOrder(createOrderRentalsRequest), MovieOrderDto.class
        );
    }

    @PutMapping("/{orderId}/return")
    public MovieOrderDto returnOrderRentals(@PathVariable("orderId") Long orderId) {
        return conversionService.convert(
                orderService.returnOrderRentals(orderId), MovieOrderDto.class
        );
    }
}