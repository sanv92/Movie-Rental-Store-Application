package com.example.movierentalstoreapplication.controllers;

import com.example.movierentalstoreapplication.dtos.MovieRentalDto;
import com.example.movierentalstoreapplication.model.movie.MovieOrder;
import com.example.movierentalstoreapplication.services.orders.OrderRentalService;
import com.example.movierentalstoreapplication.services.orders.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("/orders/{orderId}")
@RestController
public class OrderRentalController {

    private final OrderService orderService;
    private final OrderRentalService orderRentalService;
    private final ConversionService conversionService;

    @Autowired
    public OrderRentalController(
            OrderService orderService,
            OrderRentalService orderRentalService,
            ConversionService conversionService
    ) {
        this.orderService = orderService;
        this.orderRentalService = orderRentalService;
        this.conversionService = conversionService;
    }

    @GetMapping("/rentals")
    public List<MovieRentalDto> getAllRentals(@PathVariable("orderId") Long orderId) {
        MovieOrder order = orderService.findById(orderId);

        return order.getRentals()
                .stream()
                .map(rental -> conversionService.convert(rental, MovieRentalDto.class))
                .collect(Collectors.toList());
    }

    @PutMapping("/rentals/{rentalId}/return")
    public MovieRentalDto returnRental(@PathVariable("rentalId") Long rentalId) {
        return conversionService.convert(
                orderRentalService.returnRental(rentalId)
                , MovieRentalDto.class);
    }
}