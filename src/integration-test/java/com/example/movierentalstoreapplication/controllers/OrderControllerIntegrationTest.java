package com.example.movierentalstoreapplication.controllers;

import com.example.movierentalstoreapplication.dtos.MovieOrderDto;
import com.example.movierentalstoreapplication.model.movie.*;
import com.example.movierentalstoreapplication.services.orders.CreateOrderRentalsRequest;
import com.example.movierentalstoreapplication.services.orders.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerIntegrationTest extends AbstractOrderControllerIntegrationTest {

    @MockBean
    private OrderService orderService;

    @Test
    void testGetAllOrders() throws Exception {
        List<MovieOrder> movieOrders = Arrays.asList(createMovieOrder(), createMovieOrder());
        when(orderService.findAll(any(Pageable.class)))
                .thenReturn(movieOrders);

        String result = this.mockMvc.perform(
                get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(orderService, times(1)).findAll(any(Pageable.class));
        verifyNoMoreInteractions(orderService);

        String expectedResult = objectMapper.writeValueAsString(Arrays.asList(createMovieOrderDto(), createMovieOrderDto()));

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetOrderById() throws Exception {
        MovieOrderDto movieOrderDto = createMovieOrderDto();
        MovieOrder movieOrder = createMovieOrder();

        when(orderService.findById(anyLong()))
                .thenReturn(movieOrder);

        String result = mockMvc.perform(
                get("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(orderService).findById(1L);

        String expectedResult = objectMapper.writeValueAsString(movieOrderDto);

        assertEquals(expectedResult, result);
    }

    @Test
    void testCreateOrder() throws Exception {
        CreateOrderRentalsRequest createOrderRentalsRequest = new CreateOrderRentalsRequest(
                1L,
                Arrays.asList(
                        new CreateOrderRentalsRequest.Rental(1L, 1, 0),
                        new CreateOrderRentalsRequest.Rental(2L, 2, 0)
                ));

        when(orderService.createOrder(any(CreateOrderRentalsRequest.class)))
                .thenReturn(createMovieOrder());

        String result = this.mockMvc.perform(
                post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRentalsRequest))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(orderService, times(1)).createOrder(createOrderRentalsRequest);
        verifyNoMoreInteractions(orderService);

        String expectedResult = objectMapper.writeValueAsString(createMovieOrderDto());

        assertEquals(expectedResult, result);
    }

    @Test
    void testReturnOrderRentals() throws Exception {
        when(orderService.returnOrderRentals(anyLong()))
                .thenReturn(createMovieOrder());

        String result = this.mockMvc.perform(
                put("/orders/1/return")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(orderService, times(1)).returnOrderRentals(1L);
        verifyNoMoreInteractions(orderService);

        String expectedResult = objectMapper.writeValueAsString(createMovieOrderDto());

        assertEquals(expectedResult, result);
    }
}