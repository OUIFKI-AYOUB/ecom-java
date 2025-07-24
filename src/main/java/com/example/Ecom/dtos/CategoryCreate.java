package com.example.Ecom.dtos;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryCreate(

        @NotNull(message = "name is required")
        @Size(min = 2, max = 100, message = "Name must be at least 2 characters long")
        String name
) {
}
