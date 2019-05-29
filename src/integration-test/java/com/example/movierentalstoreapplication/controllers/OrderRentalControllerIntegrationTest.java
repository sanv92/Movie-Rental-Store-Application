package com.example.movierentalstoreapplication.controllers;

import com.example.movierentalstoreapplication.services.orders.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class OrderRentalControllerIntegrationTest extends AbstractOrderControllerIntegrationTest {

    @MockBean
    private OrderService orderService;

    @Test
    void testGetAllRentals() throws Exception {
        when(orderService.findById(anyLong()))
                .thenReturn(createMovieOrder());

        String result = this.mockMvc.perform(
                get("/orders/1/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(orderService, times(1)).findById(1L);
        verifyNoMoreInteractions(orderService);

        String expectedResult = objectMapper
                .writeValueAsString(
                        Arrays.asList(createMovieRentalDto(), createMovieRentalDto(), createMovieRentalDto())
                );

        assertEquals(expectedResult, result);
    }
}