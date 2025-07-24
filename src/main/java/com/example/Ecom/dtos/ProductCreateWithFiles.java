package com.example.Ecom.dtos;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record ProductCreateWithFiles(
        @NotNull String name,
        String description,
        @NotNull BigDecimal price,
        @NotNull int stock,
        @NotNull Set<UUID> categoryIds,
        List<MultipartFile> files
) {}