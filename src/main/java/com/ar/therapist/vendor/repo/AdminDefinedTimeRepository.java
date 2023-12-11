package com.ar.therapist.vendor.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ar.therapist.vendor.entity.AdminDefinedTime;

@Repository
public interface AdminDefinedTimeRepository extends JpaRepository<AdminDefinedTime, Long>{

	List<AdminDefinedTime> findAllByDate(LocalDate date);
	

}

//	@Query("SELECT NEW map(a.date AS date, a.availableTimes AS times) FROM AdminDefinedTime a WHERE a.date >= :today ORDER BY a.date")
//	List<Map<LocalDate, List<LocalTime>>> findUpcomingTimes(@Param("today") LocalDate today);

//@Query("SELECT NEW map(a.date AS date, a.availableTimes AS times) FROM AdminDefinedTime a WHERE a.date >= :today ORDER BY a.date")
//List<Map<LocalDate, List<LocalTime>>> findUpcomingTimes(LocalDate today);