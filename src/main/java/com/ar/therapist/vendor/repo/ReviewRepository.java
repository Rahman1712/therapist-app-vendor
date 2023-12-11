package com.ar.therapist.vendor.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ar.therapist.vendor.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{

	List<Review> findByUserDataUserId(Long userId);
	
	List<Review> findByUserDataUserIdAndTherapistId(Long userId, Long therapistId);

	List<Review> findByTherapistId(Long therapistId);

	Optional<Review> findByBookingId(String bookingId);


}
