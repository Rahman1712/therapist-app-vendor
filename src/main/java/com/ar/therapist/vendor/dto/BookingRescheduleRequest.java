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
public class BookingRescheduleRequest {

	private LocalDateTime rescheduleDateTime;
    private String notes;
    private String date;
    private Long timeSlotId;
    private BookingType bookingType; 
}