package com.example.hotel_booking.repository;

import com.example.hotel_booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // check overlapping bookings (VERY IMPORTANT)
    List<Booking> findByRoomIdAndCheckOutDateAfterAndCheckInDateBefore(
            Long roomId,
            LocalDate checkIn,
            LocalDate checkOut
    );
}