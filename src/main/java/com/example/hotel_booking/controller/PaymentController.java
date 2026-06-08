package com.example.hotel_booking.controller;

import com.example.hotel_booking.entity.Booking;
import com.example.hotel_booking.entity.Payment;
import com.example.hotel_booking.repository.BookingRepository;
import com.example.hotel_booking.repository.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // =========================
    // MAKE PAYMENT
    // =========================
    @PostMapping("/{bookingId}")
    public Map<String, Object> makePayment(
            @PathVariable Long bookingId,
            @RequestBody Payment paymentRequest) {

        Map<String, Object> res = new HashMap<>();

        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);

        if (bookingOpt.isEmpty()) {
            res.put("success", false);
            res.put("message", "Booking not found");
            return res;
        }

        Booking booking = bookingOpt.get();

        // ❌ prevent double payment
        if (paymentRepository.findByBookingId(bookingId) != null) {
            res.put("success", false);
            res.put("message", "Payment already exists for this booking");
            return res;
        }

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setPaymentStatus("PAID");
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);

        // ✅ update booking status
        booking.setBookingStatus("CONFIRMED");
        bookingRepository.save(booking);

        res.put("success", true);
        res.put("message", "Payment successful");
        res.put("amount", payment.getAmount());
        res.put("status", payment.getPaymentStatus());

        return res;
    }

    // =========================
    // GET PAYMENT BY BOOKING
    // =========================
    @GetMapping("/booking/{bookingId}")
    public Payment getPaymentByBooking(@PathVariable Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    // =========================
    // GET ALL PAYMENTS (ADMIN)
    // =========================
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}