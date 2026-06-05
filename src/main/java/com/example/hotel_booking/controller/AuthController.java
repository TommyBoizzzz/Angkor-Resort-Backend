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

    @PostMapping("/register")
    public String register(@RequestBody Users user) {

        Users existingUser =
                userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            return "Email already exists";
        }

        userRepository.save(user);

        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody Users user) {

        Users dbUser =
                userRepository.findByEmail(user.getEmail());

        if (dbUser == null) {
            return "Email not found";
        }

        if (!dbUser.getPassword().equals(user.getPassword())) {
            return "Incorrect password";
        }

        return "Login success";
    }
}