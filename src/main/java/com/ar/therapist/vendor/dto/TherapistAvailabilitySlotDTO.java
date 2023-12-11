package com.ar.therapist.vendor.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.ar.therapist.vendor.entity.TimeSlot;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapistAvailabilitySlotDTO {

	private Long id;
	@JsonProperty("therapist_id")
	private Long therapistId;
	private LocalDate date;
	@JsonProperty("time_slots")
	private List<TimeSlot> timeSlots;
}

/*
@JsonProperty("selected_times")
private List<LocalTime> selectedTimes;
*/