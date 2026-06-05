package com.example.hotel_booking.controller;

import com.example.hotel_booking.entity.Users;
import com.example.hotel_booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // REGISTER
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Users user) {

        Map<String, Object> res = new HashMap<>();

        if (user.getEmail() == null || user.getPassword() == null) {
            res.put("success", false);
            res.put("message", "Email and password required");
            return res;
        }

        Users existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            res.put("success", false);
            res.put("message", "Email already exists");
            return res;
        }

        userRepository.save(user);

        res.put("success", true);
        res.put("message", "User registered successfully");
        return res;
    }

    // LOGIN
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Users user) {

        Map<String, Object> res = new HashMap<>();

        if (user.getEmail() == null || user.getPassword() == null) {
            res.put("success", false);
            res.put("message", "Email and password required");
            return res;
        }

        Users dbUser = userRepository.findByEmail(user.getEmail());

        if (dbUser == null) {
            res.put("success", false);
            res.put("message", "Email not found");
            return res;
        }

        if (!dbUser.getPassword().equals(user.getPassword())) {
            res.put("success", false);
            res.put("message", "Incorrect password");
            return res;
        }

        res.put("success", true);
        res.put("message", "Login success");
        return res;
    }
}