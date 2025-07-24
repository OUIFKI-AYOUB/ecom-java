package com.example.Ecom.reposiroties;

import com.example.Ecom.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository

public interface UserRepo extends JpaRepository<Users, UUID> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);


    Optional<Users> findOneByEmail(String email);


    Optional<Users> findOneByAccountCreationToken(String token);



}
