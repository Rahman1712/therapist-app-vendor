package com.ar.therapist.vendor.dto;

import java.time.LocalDateTime;

import com.ar.therapist.vendor.entity.BookingType;
import com.ar.therapist.vendor.entity.UserData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {
	private LocalDateTime appointmentDateTime;
    private UserData userData;
    private String notes;
    private Long minutes;
	private Double amount;
	private String date;
    private Long therapistId;
    private Long timeSlotId;
    private BookingType bookingType; 
}
