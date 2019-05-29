package com.example.movierentalstoreapplication.controllers;

import com.example.movierentalstoreapplication.dtos.CustomerDto;
import com.example.movierentalstoreapplication.model.Customer;
import com.example.movierentalstoreapplication.services.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomerControllerIntegrationTest extends MockMvcIntegrationTest {

    @MockBean
    private CustomerService customerService;

    @Test
    void testGetAllCustomers() throws Exception {
        List<Customer> customers = Arrays.asList(createCustomer(), createCustomer());
        when(customerService.findAll(any()))
                .thenReturn(customers);

        String result = this.mockMvc.perform(
                get("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(customerService, times(1)).findAll(any(Pageable.class));
        verifyNoMoreInteractions(customerService);

        String expectedResult = objectMapper.writeValueAsString(Arrays.asList(createCustomerDto(), createCustomerDto()));

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetCustomerById() throws Exception {
        CustomerDto customerDto = createCustomerDto();
        Customer customer = createCustomer();

        when(customerService.findById(anyLong()))
                .thenReturn(customer);

        String result = mockMvc.perform(
                get("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(customerService).findById(1L);

        String expectedResult = objectMapper.writeValueAsString(customerDto);

        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Should have no violations, when create customer")
    void testCreateCustomer() throws Exception {
        CustomerDto customerDto = createCustomerDto();
        Customer customer = createCustomer();

        when(customerService.save(any(Customer.class)))
                .thenReturn(customer);

        String result = this.mockMvc.perform(
                post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(customerService, times(1)).save(customer);
        verifyNoMoreInteractions(customerService);

        String expectedResult = objectMapper.writeValueAsString(customerDto);

        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Should detect all invalid data, when create customer")
    void testCreateCustomerValidation() throws Exception {
        CustomerDto customerDto = createCustomerDto("F", "L", -1.0, -1);

        this.mockMvc.perform(
                post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDto))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JSON_ERROR_PATH, hasSize(4)));

        verify(customerService, never()).save(any());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        CustomerDto customerDto = createCustomerDto("First Name", "Last Name", 1.1, 2);
        Customer customer = createCustomer("First Name", "Last Name", 1.1, 2);

        when(customerService.save(anyLong(), any(Customer.class)))
                .thenReturn(customer);

        String result = this.mockMvc.perform(
                put("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(customerService, times(1)).save(1L, customer);
        verifyNoMoreInteractions(customerService);

        String expectedResult = objectMapper.writeValueAsString(customerDto);

        assertEquals(expectedResult, result);
    }

    private static Customer createCustomer() {
        return new Customer("First Name", "Last Name");
    }

    private static Customer createCustomer(String firstName, String lastName, Double balance, Integer bonusPoints) {
        return new Customer(firstName, lastName, balance, bonusPoints);
    }

    private static CustomerDto createCustomerDto() {
        return new CustomerDto()
                .setFirstName("First Name")
                .setLastName("Last Name");
    }

    private static CustomerDto createCustomerDto(String firstName, String lastName, Double balance, Integer bonusPoints) {
        return new CustomerDto()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setBalance(balance)
                .setBonusPoints(bonusPoints);
    }
}