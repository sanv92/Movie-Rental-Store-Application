package com.example.movierentalstoreapplication.services;

import com.example.movierentalstoreapplication.exceptions.ConflictException;
import com.example.movierentalstoreapplication.exceptions.ResourceNotFoundException;
import com.example.movierentalstoreapplication.model.Customer;
import com.example.movierentalstoreapplication.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public List<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    public Customer findById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public Customer save(Customer customer) {
        if (Optional.ofNullable(customerRepository
                .findByFirstNameAndLastName(customer.getFirstName(), customer.getLastName())).isPresent()) {
            throw new ConflictException();
        }

        return customerRepository.save(customer);
    }

    @Transactional
    public Customer save(Long customerId, Customer customer) {
        return customerRepository.findById(customerId).orElseThrow(ResourceNotFoundException::new)
                .setFirstName(customer.getFirstName())
                .setLastName(customer.getLastName());
    }

    @Transactional
    public Customer depositMoney(Long customerId, Double amount) {
        return customerRepository.findById(customerId)
                .orElseThrow(ResourceNotFoundException::new)
                .depositBalance(amount);
    }
}