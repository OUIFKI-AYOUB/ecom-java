package com.example.Ecom.controllers;

import com.example.Ecom.dtos.OrderCreate;
import com.example.Ecom.entities.OrderStatus;
import com.example.Ecom.entities.Orders;
import com.example.Ecom.services.OrderServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    // ✅ Create new order
    @PostMapping
    public ResponseEntity<Orders> createOrder(@RequestBody @Valid OrderCreate orderCreate) {
        Orders newOrder = orderService.createOne(orderCreate);
        return ResponseEntity.ok(newOrder);
    }

    // ✅ Get all orders
    @GetMapping
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    // ✅ Get order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Orders> getOrderById(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.findOne(orderId));
    }

    // ✅ Delete order (admin or owner)
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId) {
        orderService.deleteOne(orderId);
        return ResponseEntity.noContent().build();
    }

    // ✅ Update order status (ADMIN ONLY)
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Orders> updateOrderStatus(@PathVariable UUID orderId, @RequestParam OrderStatus status) {
        Orders updatedOrder = orderService.updateStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }
}
