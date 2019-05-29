package com.example.movierentalstoreapplication.dtos.converters;

import com.example.movierentalstoreapplication.dtos.CustomerBalanceDto;
import com.example.movierentalstoreapplication.model.Customer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CustomerToCustomerBalanceDto implements Converter<Customer, CustomerBalanceDto> {

    @Override
    public CustomerBalanceDto convert(Customer customer) {
        return new CustomerBalanceDto(customer.getBalance());
    }
}