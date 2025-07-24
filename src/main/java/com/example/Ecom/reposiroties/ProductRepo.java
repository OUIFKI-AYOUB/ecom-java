package com.example.Ecom.reposiroties;

import com.example.Ecom.entities.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepo extends JpaRepository<Product, UUID> {

     //List<Product> findByName (String name);
     List<Product> findByNameContainingIgnoreCase(String name);

}
