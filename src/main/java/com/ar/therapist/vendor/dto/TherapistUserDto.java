package com.ar.therapist.vendor.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapistUserDto {

	private Long id;
	private String fullname;
	
	private String imageUrl;
	
	private String bio;
	@JsonProperty(value = "experience_years")
	private int experienceYears;
	@JsonProperty(value = "hourly_rate")
	private double hourlyRate;//private float hourlyRate;
    @JsonProperty(value = "is_certified") 
    private boolean isCertified;
    private String qualification;
	private List<String> languages;
	private List<String> categories;
}
