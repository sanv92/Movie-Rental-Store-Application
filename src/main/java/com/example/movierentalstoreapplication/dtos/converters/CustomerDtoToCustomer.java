package com.example.movierentalstoreapplication.dtos.converters;

import com.example.movierentalstoreapplication.dtos.CustomerDto;
import com.example.movierentalstoreapplication.model.Customer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CustomerDtoToCustomer implements Converter<CustomerDto, Customer> {

    @Override
    public Customer convert(CustomerDto customerDto) {
        return new Customer(
                customerDto.getFirstName(),
                customerDto.getLastName(),
                customerDto.getBalance(),
                customerDto.getBonusPoints()
        );
    }
}
