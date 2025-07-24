package com.example.Ecom.services;


import com.example.Ecom.entities.Users;
import com.example.Ecom.reposiroties.UserRepo;
import com.example.Ecom.shared.CustomResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private UserRepo userRepo;



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        System.out.println("Looking for user with email: " + email);
        Optional<Users> account = userRepo.findOneByEmail(email);

        if(account.isEmpty()) {
            throw CustomResponseException.BadCredentials();
        }
        Users user = account.get();
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getAuthorities())

                .build();
    }
}
