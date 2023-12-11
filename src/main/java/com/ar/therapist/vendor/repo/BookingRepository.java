package com.ar.therapist.vendor.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ar.therapist.vendor.entity.Booking;
import com.ar.therapist.vendor.entity.PaymentStatus;
import com.ar.therapist.vendor.entity.Therapist;

import jakarta.transaction.Transactional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String>{

	List<Booking> findByTherapist(Therapist therapist);
	
	List<Booking> findByTherapistIdAndUserDataUserId(Long therapistId, Long userId);
	
	List<Booking> findByUserDataUserIdAndAppointmentDateTimeBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

	List<Booking> findByUserDataUserId(Long userId);
	
	@Transactional
	@Modifying
    @Query("UPDATE Booking b SET b.bookingStatus = 'COMPLETED' WHERE b.id = :bookingId")
    int updateBookingStatusToCompleted(@Param("bookingId") String bookingId);
	
	
	 List<Booking> findByUserDataUserIdAndPaymentStatus(Long userId, PaymentStatus paymentStatus);

	 List<Booking> findByTherapistIdAndPaymentStatus(Long therapistId, PaymentStatus paymentStatus);

}

