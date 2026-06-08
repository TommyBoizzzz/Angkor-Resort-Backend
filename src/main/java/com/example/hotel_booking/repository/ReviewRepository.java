package com.example.hotel_booking.repository;

import com.example.hotel_booking.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRoomId(Long roomId);
}