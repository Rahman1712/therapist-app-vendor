package com.ar.therapist.vendor.dto;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.ar.therapist.vendor.entity.Address;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TherapistInfoRequest {

	private String bio;
	@JsonProperty(value = "experience_years")
	private int experienceYears;
	
	@JsonProperty(value = "hourly_rate")
    private double hourlyRate; //private float hourlyRate;
	
	@JsonProperty(value = "is_certified") 
    private boolean isCertified;
	private String qualification;
    private Address address;
    private List<String> languages;
    private List<String> categories;
  
}

//    private MultipartFile educationalCertificate; // Field for educational certificate upload
//    private MultipartFile experienceCertificate;  // Field for experience certificate upload
//    private MultipartFile additionalCertificate; // Field for additional certificate upload
