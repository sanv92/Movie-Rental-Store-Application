package com.example.movierentalstoreapplication.controllers;

import com.example.movierentalstoreapplication.dtos.CustomerBalanceDto;
import com.example.movierentalstoreapplication.dtos.CustomerDto;
import com.example.movierentalstoreapplication.model.Customer;
import com.example.movierentalstoreapplication.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("/customers")
@RestController
public class CustomerController {

    private final CustomerService customerService;

    private final ConversionService conversionService;

    @Autowired
    public CustomerController(CustomerService customerService, ConversionService conversionService) {
        this.customerService = customerService;
        this.conversionService = conversionService;
    }

    @GetMapping
    public List<CustomerDto> getAllCustomers(
            @PageableDefault(size = Integer.MAX_VALUE, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return customerService.findAll(pageable)
                .stream()
                .map(customer -> conversionService.convert(customer, CustomerDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{customerId}")
    public CustomerDto getCustomerById(@PathVariable("customerId") Long customerId) {
        return conversionService.convert(
                customerService.findById(customerId),
                CustomerDto.class
        );
    }

    @PostMapping
    public CustomerDto createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        return conversionService.convert(
                customerService.save(conversionService.convert(customerDto, Customer.class)),
                CustomerDto.class
        );
    }

    @PutMapping("/{customerId}")
    public CustomerDto updateCustomer(@PathVariable("customerId") Long customerId, @Valid @RequestBody CustomerDto customerDto) {
        return conversionService.convert(
                customerService.save(customerId, conversionService.convert(customerDto, Customer.class)),
                CustomerDto.class
        );
    }

    @PutMapping("/{customerId}/deposit/{amount}")
    public CustomerBalanceDto depositMoneyToCustomerAccount(@PathVariable("customerId") Long customerId, @PathVariable("amount") Double amount) {
        return conversionService.convert(
                customerService.depositMoney(customerId, amount),
                CustomerBalanceDto.class
        );
    }
}