package com.ar.therapist.vendor.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ar.therapist.vendor.entity.Token;


@Repository
public interface TokenRepository extends JpaRepository<Token, Long>{

	@Query("""
		SELECT t FROM Token t INNER JOIN Therapist u ON t.therapist.id = u.id
		WHERE u.id = :therapistId AND (t.expired = false OR t.revoked = false)
	""")
	List<Token> findAllValidTokensByTherapist(Long therapistId);
	
	Optional<Token> findByToken(String token);
	
	@Query("""
		SELECT t.logged_at FROM Token t INNER JOIN Therapist u ON t.therapist.id = u.id
		WHERE u.id = :therapistId ORDER BY t.logged_at DESC LIMIT 5
	""")
	List<LocalDateTime> findLastFiveLoginsByTherapist(Long therapistId);
}
