package com.example.Ecom.reposiroties;

import com.example.Ecom.entities.ProductFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductFileRepo extends JpaRepository<ProductFile, UUID> {}
