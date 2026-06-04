package com.example.hotel_booking.controller;

import com.example.hotel_booking.entity.Users;
import com.example.hotel_booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String login(@RequestBody Users user) {

        Users dbUser = userRepository.findByUsername(user.getUsername());

        if (dbUser != null &&
                dbUser.getPassword().equals(user.getPassword())) {
            return "Login success";
        }

        return "Login failed";
    }

    @PostMapping("/register")
    public String register(@RequestBody Users user) {

        Users existingUser =
                userRepository.findByUsername(user.getUsername());

        if (existingUser != null) {
            return "Username already exists";
        }

        userRepository.save(user);

        return "User registered successfully";
    }
}