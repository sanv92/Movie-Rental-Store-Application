package com.example.movierentalstoreapplication.controllers;

import com.example.movierentalstoreapplication.dtos.CustomerBalanceDto;
import com.example.movierentalstoreapplication.dtos.CustomerDto;
import com.example.movierentalstoreapplication.model.Customer;
import com.example.movierentalstoreapplication.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private ConversionService conversionService;

    private CustomerController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        this.controller = new CustomerController(customerService, conversionService);
    }

    @Test
    void testGetAllCustomers() {
        final Pageable pageable = PageRequest.of(0, 10, new Sort(Sort.Direction.DESC, "id"));

        List<Customer> customers = Arrays.asList(createCustomer(), createCustomer());
        when(customerService.findAll(any(Pageable.class)))
                .thenReturn(customers);

        when(conversionService.convert(any(Customer.class), any()))
                .thenReturn(createCustomerDto());

        List<CustomerDto> result = controller.getAllCustomers(pageable);
        List<CustomerDto> expectedResult = Arrays.asList(createCustomerDto(), createCustomerDto());

        assertArrayEquals(expectedResult.toArray(), result.toArray());
        verify(customerService, times(1)).findAll(pageable);
        verifyNoMoreInteractions(customerService);
    }

    @Test
    void testGetCustomerById() {
        when(customerService.findById(anyLong()))
                .thenReturn(createCustomer());

        when(conversionService.convert(any(Customer.class), any()))
                .thenReturn(createCustomerDto());

        CustomerDto result = controller.getCustomerById(1L);
        CustomerDto expectedResult = createCustomerDto();

        assertEquals(expectedResult, result);
        verify(customerService, times(1)).findById(1L);
        verifyNoMoreInteractions(customerService);
    }

    @Test
    void testCreateCustomer() {
        when(conversionService.convert(any(CustomerDto.class), any()))
                .thenReturn(createCustomer());

        when(customerService.save(any(Customer.class)))
                .thenReturn(createCustomer());

        when(conversionService.convert(any(Customer.class), any()))
                .thenReturn(createCustomerDto());

        CustomerDto result = controller.createCustomer(createCustomerDto());
        CustomerDto expectedResult = createCustomerDto();

        assertEquals(expectedResult, result);
        verify(customerService, times(1)).save(createCustomer());
        verifyNoMoreInteractions(customerService);
    }

    @Test
    void testUpdateCustomer() {
        when(conversionService.convert(any(CustomerDto.class), any()))
                .thenReturn(createCustomer());

        when(customerService.save(anyLong(), any(Customer.class)))
                .thenReturn(createCustomer());

        when(conversionService.convert(any(Customer.class), any()))
                .thenReturn(createCustomerDto());

        CustomerDto result = controller.updateCustomer(1L, createCustomerDto());
        CustomerDto expectedResult = createCustomerDto();

        assertEquals(expectedResult, result);
        verify(customerService, times(1)).save(1L, createCustomer());
        verifyNoMoreInteractions(customerService);
    }

    @Test
    void testDepositMoneyToCustomerAccount() {
        when(customerService.depositMoney(anyLong(), any(Double.class)))
                .thenReturn(createCustomer());

        when(conversionService.convert(any(Customer.class), any()))
                .thenReturn(createCustomerBalanceDto());

        CustomerBalanceDto result = controller.depositMoneyToCustomerAccount(1L, (double) 99);
        CustomerBalanceDto expectedResult = createCustomerBalanceDto();

        assertEquals(expectedResult, result);
        verify(customerService, times(1)).depositMoney(1L, (double) 99);
        verifyNoMoreInteractions(customerService);
    }

    private Customer createCustomer() {
        return new Customer("First Name", "Last Name", 1, 2);
    }

    private CustomerDto createCustomerDto() {
        return new CustomerDto()
                .setFirstName("First Name")
                .setLastName("Last Name")
                .setBalance(1.0)
                .setBonusPoints(2);
    }

    private CustomerBalanceDto createCustomerBalanceDto () {
        return new CustomerBalanceDto((double) 99);
    }
}