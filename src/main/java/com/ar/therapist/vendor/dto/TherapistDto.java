package com.ar.therapist.vendor.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapistDto {

	private Long id;
	private String username;
	private String fullname;
	private String email;
	private String mobile;
	private String role;
	
	private String imageUrl;
	
	private boolean nonLocked;
	private boolean enabled;
	private boolean activated;
	private boolean submited;
	
	private LocalDateTime created;
	
	private TherapistInfoDTO therapistInfoDto;  
}
