package com.example.Ecom.config;

import com.example.Ecom.entities.Permissions;
import com.example.Ecom.entities.Role;
import com.example.Ecom.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity

public class SecurityConfig {


    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthFilter  jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){

         return new BCryptPasswordEncoder();

    }




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(c -> {
                    CorsConfigurationSource source = corsConfigurationSource();
                    c.configurationSource(source);
                        })
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {

                    auth
                            .requestMatchers(
                                    "/auth/signup",
                                    "/auth/verify/**",
                                    "/auth/verify",
                                    "/auth/login",
                                    "/auth/forgot-password/{email}",
                                    "/auth/reset-password"
                                   )
                            .permitAll()

                     //       .requestMatchers("/reviews/**").permitAll()


                            .requestMatchers(HttpMethod.GET, "/products").hasAuthority(Permissions.read_product.name())
                            .requestMatchers(HttpMethod.GET, "/products/{productId}").hasAuthority(Permissions.read_product.name())
                            .requestMatchers(HttpMethod.POST, "/products").hasAuthority(Permissions.write_product.name())
                            .requestMatchers(HttpMethod.DELETE, "/products/{productId}").hasAuthority(Permissions.delete_product.name())
                            .requestMatchers(HttpMethod.PUT, "/products/{productId}").hasAuthority(Permissions.update_product.name())

                            .requestMatchers(HttpMethod.GET, "/categories").hasAuthority(Permissions.read_category.name())
                            .requestMatchers(HttpMethod.GET, "/categories/{categoryId}").hasAuthority(Permissions.read_category.name())
                            .requestMatchers(HttpMethod.POST, "/categories").hasAuthority(Permissions.write_category.name())
                            .requestMatchers(HttpMethod.DELETE, "/categories/{categoryId}").hasAuthority(Permissions.delete_category.name())
                            .requestMatchers(HttpMethod.PUT, "/categories/{categoryId}").hasAuthority(Permissions.update_category.name())

                            .requestMatchers(HttpMethod.GET, "/reviews").hasAuthority(Permissions.read_review.name())
                            .requestMatchers(HttpMethod.GET, "/reviews/{reviewId}").hasAuthority(Permissions.read_review.name())
                            .requestMatchers(HttpMethod.POST, "/reviews").hasAuthority(Permissions.write_review.name())
                            .requestMatchers(HttpMethod.PUT, "/reviews/{reviewId}").hasAuthority(Permissions.update_review.name())
                            .requestMatchers(HttpMethod.DELETE, "/reviews/{reviewId}").hasAuthority(Permissions.delete_review.name())


                            .requestMatchers(HttpMethod.GET, "/orders").hasAuthority(Permissions.read_order.name())
                            .requestMatchers(HttpMethod.GET, "/orders/{orderId}").hasAuthority(Permissions.read_order.name())
                            .requestMatchers(HttpMethod.POST, "/orders").hasAuthority(Permissions.write_order.name())
                            .requestMatchers(HttpMethod.DELETE, "/orders/{orderId}").hasAuthority(Permissions.delete_order.name())
                            .requestMatchers(HttpMethod.PUT, "/orders/{orderId}/status").hasAuthority(Permissions.update_status_order.name())



                            .anyRequest()
                            .authenticated();                }
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .authenticationManager(authenticationManager(http));

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); ;
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS" , "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        var authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

        return authBuilder.build();
    }

}
