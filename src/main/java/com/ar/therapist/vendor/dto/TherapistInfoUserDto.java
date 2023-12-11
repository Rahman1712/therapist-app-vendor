package com.ar.therapist.vendor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapistInfoUserDto {

	private Long therapistId;
	private String fullname;
	private String imageUrl;
	private String email;
    private String mobile;
}
