package com.example.movierentalstoreapplication.repositories;

import com.example.movierentalstoreapplication.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class CustomerRepositoryIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testFindByFirstNameAndLastName() {
        Customer customer = new Customer("Eric", "Evans", 0, 0);

        Customer savedCustomer = customerRepository.save(customer);

        Customer found = customerRepository
                .findByFirstNameAndLastName("Eric", "Evans");

        assertThat(found).isEqualTo(savedCustomer);
    }
}