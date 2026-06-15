package com.example.hotel_booking.controller;

import com.example.hotel_booking.entity.Booking;
import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.entity.Users;
import com.example.hotel_booking.repository.BookingRepository;
import com.example.hotel_booking.repository.RoomRepository;
import com.example.hotel_booking.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    // =========================
    // CREATE BOOKING (MAIN LOGIC)
    // =========================
    @PostMapping
    public Map<String, Object> createBooking(@RequestBody Booking booking,
                                             @RequestParam Long userId,
                                             @RequestParam Long roomId) {

        Map<String, Object> res = new HashMap<>();

        Optional<Users> userOpt = userRepository.findById(userId);
        Optional<Room> roomOpt = roomRepository.findById(roomId);

        if (userOpt.isEmpty() || roomOpt.isEmpty()) {
            res.put("success", false);
            res.put("message", "User or Room not found");
            return res;
        }

        Room room = roomOpt.get();

        // ❌ CHECK IF ROOM IS ALREADY BOOKED IN THAT DATE RANGE
        List<Booking> conflict = bookingRepository
                .findByRoomIdAndCheckOutDateAfterAndCheckInDateBefore(
                        roomId,
                        booking.getCheckInDate(),
                        booking.getCheckOutDate()
                );

        if (!conflict.isEmpty()) {
            res.put("success", false);
            res.put("message", "Room already booked for selected dates");
            return res;
        }

        // 💰 CALCULATE PRICE
        long days = ChronoUnit.DAYS.between(
                booking.getCheckInDate(),
                booking.getCheckOutDate()
        );

        if (days <= 0) {
            res.put("success", false);
            res.put("message", "Invalid date range");
            return res;
        }

        booking.setTotalPrice(days * room.getPrice());

        booking.setUser(userOpt.get());
        booking.setRoom(room);
        booking.setBookingStatus("PENDING");

        bookingRepository.save(booking);

        res.put("success", true);
        res.put("message", "Booking created successfully");
        res.put("totalDays", days);
        res.put("totalPrice", booking.getTotalPrice());

        return res;
    }

    // =========================
    // GET ALL BOOKINGS (ADMIN)
    // =========================
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // =========================
    // GET USER BOOKINGS
    // =========================
    @GetMapping("/user/{userId}")
    public List<Booking> getUserBookings(@PathVariable Long userId) {
        return bookingRepository.findAll()
                .stream()
                .filter(b -> b.getUser().getId().equals(userId))
                .toList();
    }

    // =========================
    // CANCEL BOOKING
    // =========================
    @PutMapping("/cancel/{id}")
    public Map<String, Object> cancelBooking(@PathVariable Long id) {

        Map<String, Object> res = new HashMap<>();

        Optional<Booking> bookingOpt = bookingRepository.findById(id);

        if (bookingOpt.isEmpty()) {
            res.put("success", false);
            res.put("message", "Booking not found");
            return res;
        }

        Booking booking = bookingOpt.get();
        booking.setBookingStatus("CANCELLED");

        bookingRepository.save(booking);

        res.put("success", true);
        res.put("message", "Booking cancelled");

        return res;
    }


    @PostMapping("/reserve")
    public Map<String, Object> reserveRoom(@RequestBody Map<String, Object> req) {

        Map<String, Object> res = new HashMap<>();

        String roomType = (String) req.get("roomType");
        Long userId = Long.valueOf(req.get("userId").toString());
        int roomCount = (int) req.get("roomCount");

        List<Room> availableRooms =
                roomRepository.findByRoomTypeAndStatus(roomType, "AVAILABLE");

        if (availableRooms.size() < roomCount) {
            res.put("success", false);
            res.put("message", "Not enough rooms");
            return res;
        }

        // 🔥 RANDOM ROOM SELECTION
        Collections.shuffle(availableRooms);

        List<Room> selected = availableRooms.stream()
                .limit(roomCount)
                .toList();

        for (Room room : selected) {
            room.setStatus("PENDING"); // 🔒 lock temporarily
            roomRepository.save(room);
        }

        Booking booking = new Booking();
        booking.setUser(userRepository.findById(userId).get());
        booking.setRoom(selected.get(0)); // (or redesign for multiple rooms)
        booking.setBookingStatus("PENDING");

        bookingRepository.save(booking);

        res.put("success", true);
        res.put("bookingId", booking.getId());
        res.put("rooms", selected);

        return res;
    }
}