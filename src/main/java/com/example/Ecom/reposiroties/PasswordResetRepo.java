package com.example.Ecom.reposiroties;

import com.example.Ecom.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetRepo extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findOneByToken(String token);
}
