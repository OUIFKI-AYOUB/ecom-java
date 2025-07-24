package com.example.Ecom.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreate (
        @NotNull(message = "quantity is required")
        int quantity,
        @NotNull(message = "price is required")
        BigDecimal price,
        @NotNull(message = "product id is required")
        UUID productId

        ){
}
