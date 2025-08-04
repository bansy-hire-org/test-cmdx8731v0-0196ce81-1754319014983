package com.example.ordermanagement;

import com.example.ordermanagement.controller.OrderController;
import com.example.ordermanagement.model.Order;
import com.example.ordermanagement.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllOrders() throws Exception {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("John Doe", "Laptop", 1));
        orders.add(new Order("Jane Smith", "Mouse", 2));

        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].customerName", is("John Doe")));
    }

    @Test
    void getOrderById_existingOrder() throws Exception {
        Order order = new Order("John Doe", "Laptop", 1);
        order.setId(1L);

        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName", is("John Doe")))
                .andExpect(jsonPath("$.product", is("Laptop")));
    }

    @Test
    void getOrderById_nonExistingOrder() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createOrder() throws Exception {
        Order order = new Order("John Doe", "Laptop", 1);
        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName", is("John Doe")))
                .andExpect(jsonPath("$.product", is("Laptop")));
    }

    @Test
    void updateOrder_existingOrder() throws Exception {
        Order order = new Order("John Doe", "Laptop", 1);
        order.setId(1L);

        when(orderService.updateOrder(eq(1L), any(Order.class))).thenReturn(order);

        mockMvc.perform(put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName", is("John Doe")))
                .andExpect(jsonPath("$.product", is("Laptop")));
    }

    @Test
    void updateOrder_nonExistingOrder() throws Exception {
        Order order = new Order("John Doe", "Laptop", 1);
        when(orderService.updateOrder(eq(1L), any(Order.class))).thenThrow(new RuntimeException("Order not found"));

        mockMvc.perform(put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteOrder() throws Exception {
        doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());
    }
}
