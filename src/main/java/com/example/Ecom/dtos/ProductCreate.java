package com.example.Ecom.dtos;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record ProductCreate(

        @NotNull(message = "name is required")
        @Size(min = 2, max = 100, message = "Name must be at least 2 characters long")
        String name,



        String description,

        @NotNull(message = "price is required")
        BigDecimal price,

        @NotNull(message = "stock is required")
        int stock,


        @NotEmpty(message = "At least one category name is required")
        Set<UUID> categoryIds



) {
}
