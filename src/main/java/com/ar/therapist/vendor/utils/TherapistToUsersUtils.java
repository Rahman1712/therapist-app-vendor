package com.ar.therapist.vendor.utils;

import com.ar.therapist.vendor.dto.TherapistUserDto;
import com.ar.therapist.vendor.entity.Category;
import com.ar.therapist.vendor.entity.Therapist;

public class TherapistToUsersUtils {

	public static TherapistUserDto therapistToTherapistUserDto(Therapist therapist) {
		TherapistUserDto dto = new TherapistUserDto();
		dto.setId(therapist.getId());
		dto.setFullname(therapist.getFullname());
		dto.setImageUrl(therapist.getImageUrl());
		if(therapist.getTherapistInfo() != null) {
			dto.setBio(therapist.getTherapistInfo().getBio());
			dto.setExperienceYears(therapist.getTherapistInfo().getExperienceYears());
			dto.setHourlyRate(therapist.getTherapistInfo().getHourlyRate());
			dto.setCertified(therapist.getTherapistInfo().isCertified());
			dto.setQualification(therapist.getTherapistInfo().getQualification());
			dto.setLanguages(therapist.getTherapistInfo().getLanguages());
			dto.setCategories(therapist.getTherapistInfo().getCategories() == null
					? null
					: therapist.getTherapistInfo().getCategories()
					  .stream()
					  .map(Category::getName)
					  .toList());
		}
		return dto;
	}

}

