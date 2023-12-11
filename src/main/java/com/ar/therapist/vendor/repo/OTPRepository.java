package com.ar.therapist.vendor.repo;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ar.therapist.vendor.entity.OtpData;
import com.ar.therapist.vendor.entity.Therapist;

import jakarta.transaction.Transactional;

@Repository
public interface OTPRepository extends JpaRepository<OtpData, Long>{

	public Optional<OtpData> findByTherapistId(Long id);
	
	public Optional<OtpData> findByTherapist(Therapist therapist);

	@Modifying
	@Transactional
    @Query("UPDATE OtpData o SET o.otp = :otp, o.expirationTime = :expirationTime WHERE o.therapist.id = :therapistId")
    void updateOtpAndExpirationTimeByTherapistId(String otp, LocalDateTime expirationTime, Long therapistId);
	
//	@Query("SELECT o FROM OtpData o WHERE o.therapist.id = ?1")
//	Optional<OtpData> findByTherapistId(Long therapistid);
}
