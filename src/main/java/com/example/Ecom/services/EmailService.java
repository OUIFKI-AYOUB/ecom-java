package com.example.Ecom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${backend.origin}")
    private String ORIGIN;

    @Value("${email_user}")
    private String from;

    public void sendAccountCreationEmail(String to , String token){
        String link = ORIGIN +"/auth/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Create your Account");
        message.setText("hi please create your account using this link bellow : \n " + link);
        javaMailSender.send(message);
    }


    public void sendPasswordResetEmail(String to , String token){
        String link = ORIGIN +"/auth/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Reset your password");
        message.setText("hi click the link bellow to rest your password: \n " + link);
        javaMailSender.send(message);
    }


}
