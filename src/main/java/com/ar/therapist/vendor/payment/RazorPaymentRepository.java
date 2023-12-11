package com.ar.therapist.vendor.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RazorPaymentRepository extends JpaRepository<RazorPayment, String>{
	
}

