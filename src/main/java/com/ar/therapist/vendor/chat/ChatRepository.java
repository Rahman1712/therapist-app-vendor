package com.ar.therapist.vendor.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

	Optional<Chat> findByRoomId(String roomId);
	
	List<Chat> findByUserId(Long userId);
	List<Chat> findByTherapistId(Long therapistId);

    @Query("SELECT DISTINCT c.userId FROM Chat c WHERE c.therapistId = :therapistId")
    List<Long> findUserIdsByTherapistId(@Param("therapistId") Long therapistId);

    @Query("SELECT DISTINCT c.therapistId FROM Chat c WHERE c.userId = :userId")
    List<Long> findTherapistIdsByUserId(@Param("userId") Long userId);



}
