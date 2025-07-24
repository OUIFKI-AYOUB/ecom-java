package com.example.Ecom.dtos;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;


public record ReviewUpdate(

        @NotNull(message = "rating is required")
        int rating,


        @NotNull(message = "comment is required")
        @Size(min = 2, max = 100, message = "Comment must be at least 2 characters long")
        String comment




) {
}
