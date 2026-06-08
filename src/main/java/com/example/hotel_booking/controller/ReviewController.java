package com.example.hotel_booking.controller;

import com.example.hotel_booking.entity.Review;
import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.entity.Users;
import com.example.hotel_booking.repository.ReviewRepository;
import com.example.hotel_booking.repository.RoomRepository;
import com.example.hotel_booking.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    // =========================
    // CREATE REVIEW
    // =========================
    @PostMapping
    public Map<String, Object> createReview(
            @RequestParam Long userId,
            @RequestParam Long roomId,
            @RequestBody Review reviewRequest) {

        Map<String, Object> res = new HashMap<>();

        Optional<Users> userOpt = userRepository.findById(userId);
        Optional<Room> roomOpt = roomRepository.findById(roomId);

        if (userOpt.isEmpty() || roomOpt.isEmpty()) {
            res.put("success", false);
            res.put("message", "User or Room not found");
            return res;
        }

        if (reviewRequest.getRating() < 1 || reviewRequest.getRating() > 5) {
            res.put("success", false);
            res.put("message", "Rating must be 1-5");
            return res;
        }

        Review review = new Review();
        review.setUser(userOpt.get());
        review.setRoom(roomOpt.get());
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);

        res.put("success", true);
        res.put("message", "Review submitted successfully");

        return res;
    }

    // =========================
    // GET ALL REVIEWS
    // =========================
    @GetMapping
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // =========================
    // GET REVIEWS BY ROOM
    // =========================
    @GetMapping("/room/{roomId}")
    public List<Review> getByRoom(@PathVariable Long roomId) {
        return reviewRepository.findByRoomId(roomId);
    }

    // =========================
    // DELETE REVIEW (ADMIN)
    // =========================
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteReview(@PathVariable Long id) {

        Map<String, Object> res = new HashMap<>();

        if (!reviewRepository.existsById(id)) {
            res.put("success", false);
            res.put("message", "Review not found");
            return res;
        }

        reviewRepository.deleteById(id);

        res.put("success", true);
        res.put("message", "Review deleted");

        return res;
    }
}