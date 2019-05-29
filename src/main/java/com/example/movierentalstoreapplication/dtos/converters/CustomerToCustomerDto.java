package com.example.movierentalstoreapplication.dtos.converters;

import com.example.movierentalstoreapplication.dtos.CustomerDto;
import com.example.movierentalstoreapplication.model.Customer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CustomerToCustomerDto implements Converter<Customer, CustomerDto> {

    @Override
    public CustomerDto convert(Customer customer) {
        return new CustomerDto()
                .setId(customer.getId())
                .setFirstName(customer.getFirstName())
                .setLastName(customer.getLastName())
                .setBalance(customer.getBalance())
                .setBonusPoints(customer.getBonusPoints());
    }
}
