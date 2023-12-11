package com.ar.therapist.vendor.shared;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ar.therapist.vendor.dto.BookingDTO;
import com.ar.therapist.vendor.dto.BookingRequest;
import com.ar.therapist.vendor.dto.BookingRescheduleRequest;
import com.ar.therapist.vendor.dto.TherapistInfoUserDto;
import com.ar.therapist.vendor.entity.PaymentStatus;
import com.ar.therapist.vendor.entity.UserData;
import com.ar.therapist.vendor.payment.RazorPayment;
import com.ar.therapist.vendor.service.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/therapist-to-user")
@RequiredArgsConstructor
public class UserController {
	
	private final BookingService bookingService;
	
    @PostMapping("/book")  // booking by therapistid( in bookingRequest) 
    public ResponseEntity<BookingDTO> bookAppointment(@RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(bookingService.bookAppointment(bookingRequest));
    } 
    
    @PutMapping("/book-reschedule/byid/{bookingId}")   
    public ResponseEntity<BookingDTO> bookReschedule(
    		@PathVariable("bookingId") String bookingId,
    		@RequestBody BookingRescheduleRequest rescheduleRequest) {
    	return ResponseEntity.ok(bookingService.bookReschedule(bookingId, rescheduleRequest));
    }
    
    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<BookingDTO> cancelBooking(@PathVariable String bookingId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }
    
    @GetMapping("/booking-byid/{bookingId}")
    public ResponseEntity<BookingDTO> findByidBook(@PathVariable String bookingId) {
        return ResponseEntity.ok(bookingService.findById(bookingId));
    }
    
    @GetMapping("booking-byuserid/{userId}")
    public ResponseEntity<List<BookingDTO>> findBookingsByUserId(@PathVariable Long userId) {
    	return ResponseEntity.ok(bookingService.findAllByUserId(userId));
    }
    
    
    @PutMapping("/{id}/updatePayment")
    public BookingDTO updateBookingPayment(
            @PathVariable String id,
            @RequestBody RazorPayment razorPayment
    ) {
        return bookingService.updateBookingPayment(id, razorPayment);
    }
    
    //For chat ; to user side
    @GetMapping("/book-therapists/by-user/{userId}")
    public List<TherapistInfoUserDto> getTherapistInfosByUserIdAndPaymentStatus(
            @PathVariable Long userId
    ) {
        return bookingService.getTherapistInfosByUserIdAndPaymentStatus(userId, PaymentStatus.PAID);
    }
   
}