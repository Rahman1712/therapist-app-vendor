package com.ar.therapist.vendor.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ar.therapist.vendor.entity.TherapistAvailabilitySlot;

@Repository
public interface TherapistAvailabilitySlotRepository extends JpaRepository<TherapistAvailabilitySlot, Long>{

	List<TherapistAvailabilitySlot> findByTherapistId(Long therapistId);

    // Define the query for getting upcoming slots by therapist
    @Query("SELECT tas FROM TherapistAvailabilitySlot tas WHERE tas.therapist.id = :therapistId AND tas.date >= CURRENT_DATE")
    List<TherapistAvailabilitySlot> findUpcomingSlotsByTherapist(@Param("therapistId") Long therapistId);

    // Define the query for getting availability slots by date and therapist
    @Query("SELECT tas FROM TherapistAvailabilitySlot tas WHERE tas.therapist.id = :therapistId AND tas.date = :date")
    List<TherapistAvailabilitySlot> findByDateAndTherapist(@Param("therapistId") Long therapistId, @Param("date") LocalDate date);

}
