package com.example.Ecom.services;


import com.example.Ecom.config.JwtHelper;
import com.example.Ecom.dtos.LoginRequest;
import com.example.Ecom.dtos.ResetPasswordRequest;
import com.example.Ecom.dtos.SignupRequest;
import com.example.Ecom.dtos.SignupRequestverf;
import com.example.Ecom.entities.PasswordResetToken;
import com.example.Ecom.entities.Role;
import com.example.Ecom.entities.Users;
import com.example.Ecom.reposiroties.PasswordResetRepo;
import com.example.Ecom.reposiroties.UserRepo;
import com.example.Ecom.shared.CustomResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private PasswordResetRepo passwordResetRepo;

    @Autowired
    private EmailService emailService;

    public void signup(SignupRequest signupRequest  ) {
        if (userRepo.existsByEmail(signupRequest.email())) {
            throw CustomResponseException.BadRequest("Email already in use");
        }

        if (userRepo.existsByUsername(signupRequest.username())) {
            throw CustomResponseException.BadRequest("Username already in use");
        }

        String verificationToken = UUID.randomUUID().toString();

        Users user = new Users();
        user.setUsername(signupRequest.username());
        user.setEmail(signupRequest.email());
        user.setPassword(passwordEncoder.encode(signupRequest.password()));
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setVerified(false);  // Set to false initially
        user.setAccountCreationToken(verificationToken);  // Set the verification token

        userRepo.save(user);

        // Send verification email
        emailService.sendAccountCreationEmail(user.getEmail(), verificationToken);
    }



    @Transactional
    public void verifyAccount(SignupRequestverf signupRequestverf, String token) {
        try {
            Users user = userRepo.findOneByAccountCreationToken(token)
                    .orElseThrow(() -> CustomResponseException.ResourceNotFound("Invalid token"));

            if (user.isVerified()) {
                throw CustomResponseException.BadRequest("Account already verified");
            }

            // التحقق من تعارض البريد واسم المستخدم
            if (!user.getEmail().equals(signupRequestverf.email()) &&
                    userRepo.existsByEmail(signupRequestverf.email())) {
                throw CustomResponseException.BadRequest("Email already in use");
            }


            // تحديث معلومات المستخدم وتفعيله
            user.setEmail(signupRequestverf.email());
            user.setPassword(passwordEncoder.encode(signupRequestverf.password()));
            user.setVerified(true);
            user.setCreatedAt(LocalDateTime.now());

            userRepo.save(user);
        } catch (CustomResponseException e) {
            throw e;
        } catch (Exception e) {
            throw CustomResponseException.BadRequest("Account verification failed: " + e.getMessage());
        }
    }





    public String login(LoginRequest loginRequest) {

        // First find user by email
        Users user = userRepo.findOneByEmail(loginRequest.email())
                .orElseThrow(CustomResponseException::BadCredentials);

        if (!user.isVerified()) {
            throw CustomResponseException.BadRequest("Account not verified");
        }

        // Authenticate using email (which is the username in UserDetails)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(), // Using email as username
                        loginRequest.password()
                )
        );


        // Add custom claims to token if needed
        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("userId", user.getId());
        customClaims.put("role", user.getRole());
        customClaims.put("username", user.getUsername());

        // Generate JWT token
        return jwtHelper.generateToken(customClaims, new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), user.getAuthorities()));
    }

    @Transactional
    public void initiatePasswordReset(String email) {
        try {
            Users user = userRepo.findOneByEmail(email)
                    .orElseThrow(() -> CustomResponseException.ResourceNotFound("Account not found"));

            String token = UUID.randomUUID().toString();
            LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(15);

            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setExpiryDate(expiryDate);
            resetToken.setUser(user);

            passwordResetRepo.save(resetToken);
            emailService.sendPasswordResetEmail(user.getEmail(), token);
        } catch (Exception e) {
            throw CustomResponseException.BadRequest("Sending reset password email failed");
        }
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        PasswordResetToken resetToken = passwordResetRepo.findOneByToken(resetPasswordRequest.token())
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Invalid token"));

        boolean isTokenExpired = resetToken.getExpiryDate().isBefore(LocalDateTime.now());

        if (isTokenExpired) {
            passwordResetRepo.delete(resetToken);
            throw CustomResponseException.BadRequest("Token expired, please request a new one");
        }

        Users user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.newPassword()));
        userRepo.save(user);

        passwordResetRepo.delete(resetToken);
    }
}