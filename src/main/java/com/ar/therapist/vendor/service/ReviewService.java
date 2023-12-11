package com.ar.therapist.vendor.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ar.therapist.vendor.entity.Review;
import com.ar.therapist.vendor.exception.TherapistException;
import com.ar.therapist.vendor.repo.ReviewRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    
    public List<Review> getAllReviews(){
    	return reviewRepository.findAll();
    }
    
    public Review findById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }
    
    public long countOfReviews(){
    	return reviewRepository.count();
    }

    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserDataUserId(userId);
    }

    public List<Review> getReviewsByUserIdAndTherapistId(Long userId, Long therapistId) {
        return reviewRepository.findByUserDataUserIdAndTherapistId(userId, therapistId);
    }

    public List<Review> getReviewsByTherapistId(Long therapistId) {
        return reviewRepository.findByTherapistId(therapistId);
    }

    public Review createReview(Review review) {
    	review.setDate(LocalDate.now());
        return reviewRepository.save(review);
    }

    @Transactional
    public Review updateReview(Long id, Review updatedReview) {
        Review existingReview = reviewRepository.findById(id)
        		.orElseThrow(() -> new TherapistException("Not found review with id "+id));
        if (existingReview != null) {
            existingReview.setContent(updatedReview.getContent());
            existingReview.setRating(updatedReview.getRating());
            return reviewRepository.save(existingReview);
        }
        return null; 
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

	public Review getReviewByBookingId(String bookingId) {
		return reviewRepository.findByBookingId(bookingId).orElse(null);
	}
}
