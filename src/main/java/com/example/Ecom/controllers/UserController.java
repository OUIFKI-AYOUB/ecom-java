package com.example.Ecom.controllers;


import com.example.Ecom.dtos.LoginRequest;
import com.example.Ecom.dtos.ResetPasswordRequest;
import com.example.Ecom.dtos.SignupRequest;
import com.example.Ecom.dtos.SignupRequestverf;
import com.example.Ecom.services.UserService;
import com.example.Ecom.shared.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<GlobalResponse<String>> signup(
            @RequestBody SignupRequest signupRequest


    ){

        userService.signup(signupRequest);


        return new ResponseEntity<>(new GlobalResponse<>("Signed Up"), HttpStatus.CREATED);
    }

    @PostMapping("/verify")
    public ResponseEntity<GlobalResponse<String>> verifyAccount(
            @RequestBody SignupRequestverf signupRequestverf,
            @RequestParam("token") String token
    ) {
        userService.verifyAccount(signupRequestverf, token);
        return new ResponseEntity<>(new GlobalResponse<>("Account verified successfully"), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<String>> login(
            @RequestBody LoginRequest loginRequest
    ){

        String token = userService.login(loginRequest);


        return new ResponseEntity<>(new GlobalResponse<>(token), HttpStatus.CREATED);
    }


    @PostMapping("/forgot-password/{email}")
    public ResponseEntity<GlobalResponse<String>> forgotPassword(
            @PathVariable String email
    ){

        userService.initiatePasswordReset(email);


        return new ResponseEntity<>(new GlobalResponse<>("Password Reset email sent"), HttpStatus.CREATED);
    }


    @PostMapping("/reset-password")
    public ResponseEntity<GlobalResponse<String>> resetPassword(
            @RequestBody ResetPasswordRequest resetPasswordRequest
    ){

        userService.resetPassword(resetPasswordRequest);


        return new ResponseEntity<>(new GlobalResponse<>("Password Reset Successfully"), HttpStatus.CREATED);
    }



}


