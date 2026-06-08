package com.example.hotel_booking.controller;

import com.example.hotel_booking.entity.Users;
import com.example.hotel_booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Users user) {

        Map<String, Object> res = new HashMap<>();

        if (user.getEmail() == null || user.getPassword() == null) {
            res.put("success", false);
            res.put("message", "Email and password required");
            return res;
        }

        Optional<Users> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            res.put("success", false);
            res.put("message", "Email already exists");
            return res;
        }

        // DEFAULT VALUES
        user.setRole("GUEST");

        if (user.getUsername() == null) {
            user.setUsername(user.getEmail().split("@")[0]);
        }

        if (user.getPhoneNumber() == null) {
            user.setPhoneNumber("N/A");
        }

        userRepository.save(user);

        res.put("success", true);
        res.put("message", "User registered successfully");

        return res;
    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Users user) {

        Map<String, Object> res = new HashMap<>();

        if (user.getEmail() == null || user.getPassword() == null) {
            res.put("success", false);
            res.put("message", "Email and password required");
            return res;
        }

        Optional<Users> dbUserOpt = userRepository.findByEmail(user.getEmail());

        if (dbUserOpt.isEmpty()) {
            res.put("success", false);
            res.put("message", "Email not found");
            return res;
        }

        Users dbUser = dbUserOpt.get();

        if (!dbUser.getPassword().equals(user.getPassword())) {
            res.put("success", false);
            res.put("message", "Incorrect password");
            return res;
        }

        res.put("success", true);
        res.put("message", "Login success");
        res.put("user", dbUser);

        return res;
    }
}