package com.example.hotel_booking.controller;

import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    // GET ALL ROOMS
    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    // CREATE ROOM
    @PostMapping
    public Map<String, Object> createRoom(@RequestBody Room room) {

        Map<String, Object> res = new HashMap<>();

        room.setStatus("AVAILABLE");
        roomRepository.save(room);

        res.put("success", true);
        res.put("message", "Room created");

        return res;
    }

    // UPDATE ROOM
    @PutMapping("/{id}")
    public Map<String, Object> updateRoom(@PathVariable Long id,
                                          @RequestBody Room newRoom) {

        Map<String, Object> res = new HashMap<>();

        Optional<Room> opt = roomRepository.findById(id);

        if (opt.isEmpty()) {
            res.put("success", false);
            res.put("message", "Not found");
            return res;
        }

        Room room = opt.get();
        room.setRoomNumber(newRoom.getRoomNumber());
        room.setRoomType(newRoom.getRoomType());
        room.setPrice(newRoom.getPrice());
        room.setStatus(newRoom.getStatus());

        roomRepository.save(room);

        res.put("success", true);
        res.put("message", "Updated");

        return res;
    }

    // DELETE ROOM
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteRoom(@PathVariable Long id) {

        Map<String, Object> res = new HashMap<>();

        roomRepository.deleteById(id);

        res.put("success", true);
        return res;
    }

    // AVAILABLE COUNT (IMPORTANT FOR YOUR UI)
    @GetMapping("/available/{roomType}")
    public long getAvailable(@PathVariable String roomType) {
        return roomRepository.countByRoomTypeAndStatus(roomType, "AVAILABLE");
    }

    // STATS
    @GetMapping("/stats/{roomType}")
    public Map<String, Object> stats(@PathVariable String roomType) {

        Map<String, Object> res = new HashMap<>();

        res.put("total", roomRepository.countByRoomType(roomType));
        res.put("available", roomRepository.countByRoomTypeAndStatus(roomType, "AVAILABLE"));
        res.put("booked", roomRepository.countByRoomTypeAndStatus(roomType, "BOOKED"));

        return res;
    }
}