package com.ar.therapist.vendor.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ar.therapist.vendor.dto.BookingDTO;
import com.ar.therapist.vendor.dto.BookingRequest;
import com.ar.therapist.vendor.entity.Booking;
import com.ar.therapist.vendor.entity.PaymentStatus;
import com.ar.therapist.vendor.entity.UserData;
import com.ar.therapist.vendor.payment.Payment;
import com.ar.therapist.vendor.payment.PaymentService;
import com.ar.therapist.vendor.payment.RazorPayment;
import com.ar.therapist.vendor.service.BookingService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/therapist/{therapistId}/user/{userId}")
    public List<Booking> findBookingsByTherapistAndUser(
            @PathVariable Long therapistId, 
            @PathVariable Long userId) {
        return bookingService.findBookingsByTherapistIdAndUserId(therapistId, userId);
    }
    
    @PostMapping("/book")  // booking by therapistid( in bookingRequest) 
    public ResponseEntity<BookingDTO> bookAppointment(@RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(bookingService.bookAppointment(bookingRequest));
    }
    
    @GetMapping("/has-booked-today/{userId}")
    public ResponseEntity<Boolean> hasUserBookedToday(@PathVariable Long userId) {
        boolean hasBookedToday = bookingService.hasUserBookedToday(userId);
        return ResponseEntity.ok(hasBookedToday);
    }
    
    @GetMapping("/has-booked-on-date/{userId}")
    public ResponseEntity<Boolean> hasUserBookedOnDate(
        @PathVariable Long userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        boolean hasBookedOnDate = bookingService.hasUserBookedOnDate(userId, date);
        return ResponseEntity.ok(hasBookedOnDate);
    }
    
    @PostMapping("/cancel/{bookingId}")
    public ResponseEntity<BookingDTO> cancelBooking(@PathVariable String bookingId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }
    
    @GetMapping("/booking-byid/{bookingId}")
    public ResponseEntity<BookingDTO> findByidBook(@PathVariable String bookingId) {
        return ResponseEntity.ok(bookingService.findById(bookingId));
    }
    
    @GetMapping("/booking-by-therapistid/{therapistId}")
    public ResponseEntity<List<BookingDTO>> findBookingsByTherapistId(@PathVariable Long therapistId) {
    	return ResponseEntity.ok(bookingService.findAllByTherapistId(therapistId));
    }

    @PutMapping("/update-booking-completed/{bookingId}")
    public ResponseEntity<BookingDTO> updateCompletedById(@PathVariable String bookingId) {
        return ResponseEntity.ok(bookingService.updateCompletedById(bookingId));
    }
   
    //For chat ; to therapist side
    @GetMapping("/book-users/by-therapist/{therapistId}")
    public List<UserData> getUserDatasByTherapistIdAndPaymentStatus(
            @PathVariable Long therapistId
    ) {
    	System.err.println("===============##########&&&&&&&&&&&&&&&&&");
        return bookingService.getUserDatasByTherapistIdAndPaymentStatus(therapistId, PaymentStatus.PAID);
    }
}

/*
    @PostMapping("/book")
    public Booking bookAppointment(
            @RequestParam Long therapistId,
            @RequestBody UserDataDTO userDataDTO,
            //@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date appointmentDate
            @RequestParam String appointmentDate, // Date in ISO format (e.g., "2023-10-23")
            @RequestParam String appointmentTime // Time in ISO format (e.g., "15:30:00")
            
            ) {
        return bookingService.bookAppointment(therapistId, userDataDTO, appointmentDate);
    }
 */
