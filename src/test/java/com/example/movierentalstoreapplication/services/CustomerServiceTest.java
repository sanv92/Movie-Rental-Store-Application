package com.example.movierentalstoreapplication.services;

import com.example.movierentalstoreapplication.exceptions.ConflictException;
import com.example.movierentalstoreapplication.exceptions.ResourceNotFoundException;
import com.example.movierentalstoreapplication.model.Customer;
import com.example.movierentalstoreapplication.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        this.customerService = new CustomerService(customerRepository);
    }

    @Test
    void testFindById() {
        when(customerRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Customer()));

        assertNotNull(customerService.findById(1L));
        verify(customerRepository).findById(anyLong());
    }

    @Test
    void throwExceptionWhen_FindByIdAndCustomerNotExists() {
        when(customerRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> customerService.findById(1L)
        );
        verify(customerRepository).findById(1L);
    }

    @Test
    void testCreateCustomer() {
        Customer customer = createCustomer();

        when(customerRepository.findByFirstNameAndLastName("First name", "Last name"))
                .thenReturn(null);

        when(customerRepository.save(customer))
                .thenReturn(customer);

        assertNotNull(customerService.save(customer));
        verify(customerRepository).findByFirstNameAndLastName("First name", "Last name");
        verify(customerRepository).save(customer);
    }

    @Test
    void throwExceptionWhen_CreateCustomerAndCustomerAlreadyExists() {
        Customer customer = createCustomer();

        when(customerRepository.findByFirstNameAndLastName("First name", "Last name"))
                .thenReturn(customer);

        assertThrows(ConflictException.class,
                () -> customerService.save(customer)
        );
        verify(customerRepository).findByFirstNameAndLastName("First name", "Last name");
        verify(customerRepository, never()).save(any());
    }

    @Test
    void testUpdateCustomer() {
        Customer customer = new Customer("First Name2", "Last Name2");
        Customer expectedResult = new Customer("First Name", "Last Name");

        when(customerRepository.findById(anyLong()))
                .thenReturn(Optional.of(customer));

        Customer result = customerService.save(1L, expectedResult);

        verify(customerRepository).findById(1L);
        assertEquals(expectedResult, result);
    }

    @Test
    void throwExceptionWhen_UpdateCustomerAndCustomerNotFound() {
        Customer customer = mock(Customer.class);

        when(customerRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        when(customerRepository.save(any()))
                .thenReturn(customer);

        assertThrows(ResourceNotFoundException.class,
                () -> customerService.save(1L, customer)
        );
        verify(customerRepository).findById(1L);
        verify(customerRepository, never()).save(any());
    }

    @Test
    void testDepositMoney() {
        Customer customer = createCustomer();

        when(customerRepository.findById(anyLong()))
                .thenReturn(Optional.of(customer));

        when(customerRepository.save(any()))
                .thenReturn(customer);

        Customer customerResult = customerService.depositMoney(1L, (double) 10);

        assertEquals(customer, customerResult);
        assertEquals(10, customerResult.getBalance());
        verify(customerRepository).findById(1L);
    }

    @Test
    void throwExceptionWhen_DepositCustomerAccountAndCustomerNotFound() {
        when(customerRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> customerService.depositMoney(1L, (double) 10)
        );
        verify(customerRepository).findById(1L);
        verify(customerRepository, never()).save(any());
    }

    private Customer createCustomer() {
        return new Customer("First name", "Last name", 0, 0);
    }
}