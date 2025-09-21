package com.abc.authlogintest.controller;

import com.abc.authlogintest.dtos.UserDTO;
import com.abc.authlogintest.entity.User;
import com.abc.authlogintest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // ✅ Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(userService::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Get all users
    @GetMapping("/users")
    public List<UserDTO> allUsers() {
        return userService.findAll();
    }

    // ✅ Register new user
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        if (userService.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("User already exists with email: " + userDTO.getEmail());
        }

        UserDTO savedUser = userService.register(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return userService.update(id, userDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }




    // ✅ Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid email or password"));
    }


    // ✅ CORS Config
    @Value("${client.url}")
    private String clientUrl;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(clientUrl)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
