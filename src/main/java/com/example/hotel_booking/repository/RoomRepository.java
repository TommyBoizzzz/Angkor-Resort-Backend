package com.example.hotel_booking.repository;

import com.example.hotel_booking.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    long countByRoomType(String roomType);

    long countByRoomTypeAndStatus(String roomType, String status);

    // ✅ ADD THIS (required for random booking)
    List<Room> findByRoomTypeAndStatus(String roomType, String status);
}