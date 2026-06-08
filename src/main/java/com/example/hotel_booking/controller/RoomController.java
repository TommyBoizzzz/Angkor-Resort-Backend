package com.example.hotel_booking.controller;

import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    // =========================
    // GET ALL ROOMS
    // =========================
    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // =========================
    // GET ROOM BY ID
    // =========================
    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    // =========================
    // CREATE ROOM
    // =========================
    @PostMapping
    public Map<String, Object> createRoom(@RequestBody Room room) {

        Map<String, Object> res = new HashMap<>();

        if (room.getRoomNumber() == null || room.getPrice() == null) {
            res.put("success", false);
            res.put("message", "Room number and price are required");
            return res;
        }

        room.setStatus("AVAILABLE");

        roomRepository.save(room);

        res.put("success", true);
        res.put("message", "Room created successfully");

        return res;
    }

    // =========================
    // UPDATE ROOM
    // =========================
    @PutMapping("/{id}")
    public Map<String, Object> updateRoom(@PathVariable Long id, @RequestBody Room newRoom) {

        Map<String, Object> res = new HashMap<>();

        Optional<Room> roomOpt = roomRepository.findById(id);

        if (roomOpt.isEmpty()) {
            res.put("success", false);
            res.put("message", "Room not found");
            return res;
        }

        Room room = roomOpt.get();

        room.setRoomNumber(newRoom.getRoomNumber());
        room.setRoomType(newRoom.getRoomType());
        room.setPrice(newRoom.getPrice());
        room.setStatus(newRoom.getStatus());

        roomRepository.save(room);

        res.put("success", true);
        res.put("message", "Room updated successfully");

        return res;
    }

    // =========================
    // DELETE ROOM
    // =========================
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteRoom(@PathVariable Long id) {

        Map<String, Object> res = new HashMap<>();

        if (!roomRepository.existsById(id)) {
            res.put("success", false);
            res.put("message", "Room not found");
            return res;
        }

        roomRepository.deleteById(id);

        res.put("success", true);
        res.put("message", "Room deleted successfully");

        return res;
    }
}