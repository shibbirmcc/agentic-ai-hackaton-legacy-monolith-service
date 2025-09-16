package com.legacy.monolith.service;

import com.legacy.monolith.entity.Order;
import com.legacy.monolith.entity.User;
import com.legacy.monolith.repository.OrderRepository;
import com.legacy.monolith.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@Transactional
public class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateOrderWithRealUser() {
        // Create a real user
        User user = new User("testuser", "test@example.com", "Test User");
        User savedUser = userService.createUser(user);

        // Create order
        Order order = new Order();
        order.setUser(savedUser);
        order.setProductName("Test Product");
        order.setQuantity(2);
        order.setPrice(new BigDecimal("99.99"));
        order.setNotes("Integration test order");

        Order savedOrder = orderService.createOrder(order);

        assertNotNull(savedOrder.getId());
        assertNotNull(savedOrder.getOrderNumber());
        assertEquals("PENDING", savedOrder.getOrderStatus());
        assertEquals(savedUser.getId(), savedOrder.getUserId());
    }

    @Test
    public void testOrderBatchProcessing() {
        // Create user and orders
        User user = new User("batchuser", "batch@example.com", "Batch User");
        User savedUser = userService.createUser(user);

        // Create multiple orders
        for (int i = 0; i < 3; i++) {
            Order order = new Order();
            order.setUser(savedUser);
            order.setProductName("Batch Product " + i);
            order.setQuantity(1);
            order.setPrice(new BigDecimal("10.00"));
            orderService.createOrder(order);
        }

        List<Order> orders = orderService.getOrdersByUserId(savedUser.getId());
        assertEquals(3, orders.size());

        // Test batch processing
        orderService.processOrderBatch();
        
        // Verify orders are still pending (not expired yet)
        List<Order> pendingOrders = orderRepository.findByOrderStatus("PENDING");
        assertTrue(pendingOrders.size() >= 3);
    }

    @Test
    public void testHighValueOrderDetection() {
        User user = new User("richuser", "rich@example.com", "Rich User");
        User savedUser = userService.createUser(user);

        Order highValueOrder = new Order();
        highValueOrder.setUser(savedUser);
        highValueOrder.setProductName("Expensive Item");
        highValueOrder.setQuantity(1);
        highValueOrder.setPrice(new BigDecimal("15000.00")); // High value

        Order savedOrder = orderService.createOrder(highValueOrder);
        assertNotNull(savedOrder);
        assertEquals(new BigDecimal("15000.00"), savedOrder.getPrice());
    }

    @Test
    public void testBulkOrderDetection() {
        User user = new User("bulkuser", "bulk@example.com", "Bulk User");
        User savedUser = userService.createUser(user);

        Order bulkOrder = new Order();
        bulkOrder.setUser(savedUser);
        bulkOrder.setProductName("Bulk Item");
        bulkOrder.setQuantity(150); // Bulk quantity
        bulkOrder.setPrice(new BigDecimal("5.00"));

        Order savedOrder = orderService.createOrder(bulkOrder);
        assertNotNull(savedOrder);
        assertEquals(Integer.valueOf(150), savedOrder.getQuantity());
    }
}
