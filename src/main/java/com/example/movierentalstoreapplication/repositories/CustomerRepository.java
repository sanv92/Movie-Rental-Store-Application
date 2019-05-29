package com.example.movierentalstoreapplication.repositories;

import com.example.movierentalstoreapplication.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByFirstNameAndLastName(String firstName, String lastName);
}