package com.example.Ecom.reposiroties;

import com.example.Ecom.entities.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepo extends JpaRepository<Reviews, UUID> {
}
