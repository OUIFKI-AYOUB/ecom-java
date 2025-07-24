package com.example.Ecom.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "users")

public class Users implements UserDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @Column(name = "username" , nullable = false,unique = true)
    private String username;

    @Column(name = "password" , nullable = false)
    private String password;

    @Column(name = "email" , nullable = false,unique = true)
    private String email;
    @Column(name = "role" , nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.valueOf("USER");

    @Column(name = "is_verified",columnDefinition = "boolean default false", nullable = false)
    private boolean isVerified;

    @Column(name = "account_creation_token")
    private String accountCreationToken;


    @Column(name = "created_at" , nullable = false)
    private LocalDateTime createdAt;

    @Override
    public String getUsername() {
        return this.email; // This is crucial - make it return email
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
        authorities.addAll(role.getPermissions().stream()
                        .map(permissions -> new SimpleGrantedAuthority(permissions.name()))
                .collect(Collectors.toSet())
        );
        return authorities;
    }
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();
}
