    package com.example.hotel_booking.controller;

    import com.example.hotel_booking.entity.Users;
    import com.example.hotel_booking.repository.UserRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;

    import java.util.*;

    @RestController
    @RequestMapping("/api/users")
    @CrossOrigin(origins = "*")
    public class UserController {

        @Autowired
        private UserRepository userRepository;

        // =========================
        // GET ALL USERS
        // =========================
        @GetMapping
        public List<Users> getAllUsers() {
            return userRepository.findAll();
        }

        // =========================
        // GET USER BY ID
        // =========================
        @GetMapping("/{id}")
        public Users getUserById(@PathVariable Long id) {
            return userRepository.findById(id).orElse(null);
        }

        // =========================
        // CREATE USER
        // =========================
        @PostMapping
        public Map<String, Object> createUser(@RequestBody Users user) {

            Map<String, Object> res = new HashMap<>();

            if (user.getEmail() == null || user.getPassword() == null) {
                res.put("success", false);
                res.put("message", "Email and password required");
                return res;
            }

            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                res.put("success", false);
                res.put("message", "Email already exists");
                return res;
            }

            if (user.getUsername() == null || user.getUsername().isBlank()) {
                user.setUsername(user.getEmail().split("@")[0]);
            }

            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("GUEST");
            }

            if (user.getPhoneNumber() == null) {
                user.setPhoneNumber("N/A");
            }

            userRepository.save(user);

            res.put("success", true);
            res.put("message", "User created successfully");

            return res;
        }

        // =========================
        // UPDATE USER
        // =========================
        @PutMapping("/{id}")
        public Map<String, Object> updateUser(
                @PathVariable Long id,
                @RequestBody Users newUser) {

            Map<String, Object> res = new HashMap<>();

            Optional<Users> optionalUser = userRepository.findById(id);

            if (optionalUser.isEmpty()) {
                res.put("success", false);
                res.put("message", "User not found");
                return res;
            }

            Users user = optionalUser.get();

            user.setUsername(newUser.getUsername());
            user.setEmail(newUser.getEmail());

            if (newUser.getPassword() != null && !newUser.getPassword().isBlank()) {
                user.setPassword(newUser.getPassword());
            }

            user.setRole(newUser.getRole());
            user.setPhoneNumber(newUser.getPhoneNumber());
            user.setDateOfBirth(newUser.getDateOfBirth());

            userRepository.save(user);

            res.put("success", true);
            res.put("message", "User updated successfully");

            return res;
        }

        // =========================
        // DELETE USER
        // =========================
        @DeleteMapping("/{id}")
        public Map<String, Object> deleteUser(@PathVariable Long id) {

            Map<String, Object> res = new HashMap<>();

            if (!userRepository.existsById(id)) {
                res.put("success", false);
                res.put("message", "User not found");
                return res;
            }

            userRepository.deleteById(id);

            res.put("success", true);
            res.put("message", "User deleted successfully");

            return res;
        }
    }