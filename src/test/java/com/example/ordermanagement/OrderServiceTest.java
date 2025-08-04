package com.example.ordermanagement;

import com.example.ordermanagement.model.Order;
import com.example.ordermanagement.repository.OrderRepository;
import com.example.ordermanagement.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void getAllOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("John Doe", "Laptop", 1));
        orders.add(new Order("Jane Smith", "Mouse", 2));

        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getCustomerName());
    }

    @Test
    void getOrderById_existingOrder() {
        Order order = new Order("John Doe", "Laptop", 1);
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.getOrderById(1L);

        assertTrue(result.isPresent());
        assertEquals("Laptop", result.get().getProduct());
    }

    @Test
    void getOrderById_nonExistingOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Order> result = orderService.getOrderById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void createOrder() {
        Order order = new Order("John Doe", "Laptop", 1);
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.createOrder(order);

        assertEquals("John Doe", result.getCustomerName());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void updateOrder_existingOrder() {
        Order existingOrder = new Order("John Doe", "Laptop", 1);
        existingOrder.setId(1L);

        Order updatedOrderDetails = new Order("Jane Smith", "Mouse", 2);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrderDetails);

        Order result = orderService.updateOrder(1L, updatedOrderDetails);

        assertEquals("Jane Smith", result.getCustomerName());
        assertEquals("Mouse", result.getProduct());
        assertEquals(2, result.getQuantity());
    }

    @Test
    void updateOrder_nonExistingOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Order updatedOrderDetails = new Order("Jane Smith", "Mouse", 2);

        assertThrows(RuntimeException.class, () -> orderService.updateOrder(1L, updatedOrderDetails));
    }

    @Test
    void deleteOrder() {
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteOrder(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }
}
