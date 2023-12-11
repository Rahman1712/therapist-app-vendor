package com.ar.therapist.vendor.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ar.therapist.vendor.entity.BookingStatus;
import com.ar.therapist.vendor.entity.BookingType;
import com.ar.therapist.vendor.entity.PaymentStatus;
import com.ar.therapist.vendor.entity.TimeSlot;
import com.ar.therapist.vendor.entity.UserData;
import com.ar.therapist.vendor.payment.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {

	private String id;
	private LocalDateTime appointmentDateTime;
	private LocalDateTime rescheduleDateTime;
	private LocalDateTime cancellationDateTime;
	private String date;
	private String notes;
	private Long minutes;
	private Double amount;
	private TimeSlot timeSlot;
	private UserData userData;
	private TherapistInfoUserDto therapistInfo; // therapistInfo ithanu name vendathu
	private BookingStatus bookingStatus;
	private PaymentStatus paymentStatus;
	private BookingType bookingType;
	private Payment payment;
}
