package com.ar.therapist.vendor.shared;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ar.therapist.vendor.dto.BookingDTO;
import com.ar.therapist.vendor.entity.Review;
import com.ar.therapist.vendor.payment.PaymentDto;
import com.ar.therapist.vendor.payment.PaymentService;
import com.ar.therapist.vendor.service.BookingService;
import com.ar.therapist.vendor.service.CategoryService;
import com.ar.therapist.vendor.service.ReviewService;
import com.ar.therapist.vendor.service.TherapistInfoService;
import com.ar.therapist.vendor.service.TherapistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/public-to-admin")
@RequiredArgsConstructor
public class AdminPublicController {

	private final TherapistService therapistService;
	private final TherapistInfoService therapistInfoService;
	private final CategoryService categoryService;
	private final BookingService bookingService;
	private final ReviewService reviewService;
	private final PaymentService paymentService;
	
	@GetMapping("/all-reviews")
    public List<Review> getAllReviews(){
    	return reviewService.getAllReviews();
    }
	@GetMapping("/count-reviews")
    public Long countOfReviews(){
    	return reviewService.countOfReviews();
    }
    @GetMapping("/byid-review/{id}")
    public Review reviewFindById(@PathVariable("id") Long id) {
    	return reviewService.findById(id);
    }
	
    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingDTO>> findAllBookings() {
    	return ResponseEntity.ok(bookingService.findAllBookings());
    }
    @GetMapping("/count-bookings")
    public Long countOfBookings(){
    	return bookingService.countOfBookings();
    }
    @GetMapping("/byid-booking/{id}")
    public BookingDTO bookingFindById(@PathVariable("id") String id) {
    	return bookingService.findById(id);
    }
	
    @GetMapping("/all-payments")
	public List<PaymentDto> getAllPayments() {
		return paymentService.getAllPayments();
	}
    @GetMapping("/count-payments")
    public Long countOfPayments(){
    	return paymentService.countOfPayments();
    }
    @GetMapping("/byid-payment/{id}")
    public PaymentDto paymentFindById(@PathVariable("id") String pid) {
    	return paymentService.findById(pid);
    }
    
    
}
