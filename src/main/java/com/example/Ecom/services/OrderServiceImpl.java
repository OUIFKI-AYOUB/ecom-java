package com.example.Ecom.services;

import com.example.Ecom.abstracts.OrderService;
import com.example.Ecom.abstracts.ReviewService;
import com.example.Ecom.dtos.OrderCreate;
import com.example.Ecom.dtos.ReviewCreate;
import com.example.Ecom.dtos.ReviewUpdate;
import com.example.Ecom.entities.*;
import com.example.Ecom.reposiroties.OrderRepo;
import com.example.Ecom.reposiroties.ProductRepo;
import com.example.Ecom.reposiroties.UserRepo;
import com.example.Ecom.shared.CustomResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    // Helper to get current authenticated user from Spring Security
    private Users getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepo.findOneByEmail(email)
                .orElseThrow(() -> CustomResponseException
                        .Unauthorized("User not found or not authenticated"));
    }

    // Helper to check if current user is ADMIN
    private boolean isAdmin(Users user) {
        return user.getRole().equals(Role.ADMIN);
    }

    @Override
    public List<Orders> findAll() {
        return orderRepo.findAll();
    }


    @Override
    public Orders findOne(UUID orderID) {
        Orders order = orderRepo.findById(orderID)
                .orElseThrow(() -> CustomResponseException
                        .ResourceNotFound("Order with ID " + orderID + " not found"));

        return order;
    }

    @Override
    public void deleteOne(UUID orderID) {

        Orders order = orderRepo.findById(orderID)
                .orElseThrow(() -> CustomResponseException
                        .ResourceNotFound("Order with ID " + orderID + " not found"));

        Users currentUser = getCurrentUser();

        if (!isAdmin(currentUser)) {
            throw CustomResponseException.Unauthorized("Only admins can update order status.");
        }

        orderRepo.deleteById(orderID);

    }

    @Override
    public Orders createOne(OrderCreate orderCreate) {

        Product product = productRepo.findById(orderCreate.productId()).
                orElseThrow(() -> CustomResponseException
                        .ResourceNotFound("Product with ID " + orderCreate.productId() + " not found"));

        Users user = getCurrentUser();

        Orders order = new Orders();
        order.setQuantity(orderCreate.quantity());
        order.setPrice(orderCreate.price());
        order.setUser(user);
        order.setProduct(product);
        order.setCreatedAt(LocalDateTime.now());
        return orderRepo.save(order);
    }

    public Orders updateStatus(UUID orderId, OrderStatus newStatus) {
        Users currentUser = getCurrentUser();

        if (!isAdmin(currentUser)) {
            throw CustomResponseException.Unauthorized("Only admins can update order status.");
        }

        Orders order = orderRepo.findById(orderId)
                .orElseThrow(() -> CustomResponseException
                        .ResourceNotFound("Order not found"));

        // Only update if status is changed
        if (order.getStatus() != newStatus) {
            Product product = order.getProduct();

            if (newStatus == OrderStatus.CONFIRMED) {
                int remainingStock = product.getStock() - order.getQuantity();

                if (remainingStock < 0) {
                    throw CustomResponseException.BadRequest("Not enough stock to confirm the order.");
                }

                product.setStock(remainingStock);
                productRepo.save(product);
            } else if (newStatus == OrderStatus.CANCELLED) {
                // Revert stock when cancelled
                int restoredStock = product.getStock() + order.getQuantity();
                product.setStock(restoredStock);
                productRepo.save(product);
            }

            // Now update status
            order.setStatus(newStatus);
            return orderRepo.save(order);
        }

        return order; // No changes made
    }


}
