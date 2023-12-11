//package com.ar.therapist.vendor.utils;
//
//import java.util.Base64;
//import java.util.stream.Collectors;
//
//import com.ar.therapist.vendor.dto.BookingDTO;
//import com.ar.therapist.vendor.dto.CategoryDTO;
//import com.ar.therapist.vendor.dto.TherapistAvailabilitySlotDTO;
//import com.ar.therapist.vendor.dto.TherapistDto;
//import com.ar.therapist.vendor.dto.TherapistInfoDTO;
//import com.ar.therapist.vendor.dto.TherapistInfoUserDto;
//import com.ar.therapist.vendor.dto.TherapistUserDto;
//import com.ar.therapist.vendor.entity.Booking;
//import com.ar.therapist.vendor.entity.Category;
//import com.ar.therapist.vendor.entity.Therapist;
//import com.ar.therapist.vendor.entity.TherapistAvailabilitySlot;
//import com.ar.therapist.vendor.entity.TherapistInfo;
//
//public class TherapistUtils2 {
//
//	public static TherapistDto therapistToTherapistDto(Therapist therapist) {
//		return TherapistDto.builder()
//				.id(therapist.getId())
//				.username(therapist.getUsername())
//				.fullname(therapist.getFullname())
//				.email(therapist.getEmail())
//				.mobile(therapist.getMobile())
//				.role(therapist.getRole().name())
//				.nonLocked(therapist.isNonLocked())
//				.enabled(therapist.isEnabled())
//				.activated(therapist.isActivated())
//				.submited(therapist.isSubmited())
//				.created(therapist.getCreated())
//				.imageUrl(therapist.getImageUrl())
////				.image(therapist.getImage() == null ? null
////						// : therapist.getImage()
////						: ImageUtils.decompress(therapist.getImage()))
////				.imageName(therapist.getImageName())
////				.imageType(therapist.getImageType())
//				.therapistInfoDto(therapist.getTherapistInfo() == null ? null
//						: therapistInfoToDTOClass(therapist.getTherapistInfo()))
//				.build();
//	}
//
//	public static TherapistInfoDTO therapistInfoToDTOClass(TherapistInfo info) {
//		return TherapistInfoDTO.builder().id(info.getId()).bio(info.getBio()).therapistId(info.getTherapist().getId())
//				.experienceYears(info.getExperienceYears()).hourlyRate(info.getHourlyRate())
//				.isCertified(info.isCertified())
//				.qualification(info.getQualification())
//				.address(info.getAddress())
//				.languages(info.getLanguages())
//				.categories(info.getCategories().stream()
//						// .map(Category::getName) // for name only
//						.map(TherapistUtils2::categoryToCategoryDTO).collect(Collectors.toList()))
//				// .educationalCertificate(info.getEducationalCertificate() == null ? null :
//				// FileUtils.decompress(info.getEducationalCertificate()))
//				// .experienceCertificate(info.getExperienceCertificate() == null ? null :
//				// FileUtils.decompress(info.getExperienceCertificate()))
//				// .additionalCertificate(info.getAdditionalCertificate() == null ? null :
//				// FileUtils.decompress(info.getAdditionalCertificate()))
//				.educationalCertificate(info.getEducationalCertificate() == null ? null
//						: Base64.getDecoder().decode(info.getEducationalCertificate()))
//				.experienceCertificate(info.getExperienceCertificate() == null ? null
//						: Base64.getDecoder().decode(info.getExperienceCertificate()))
//				.additionalCertificate(info.getAdditionalCertificate() == null ? null
//						: Base64.getDecoder().decode(info.getAdditionalCertificate()))
//				.educationalCertificateType(info.getEducationalCertificateType())
//				.experienceCertificateType(info.getExperienceCertificateType())
//				.additionalCertificateType(info.getAdditionalCertificateType())
//				.build();
//	}
//
//	public static CategoryDTO categoryToCategoryDTO(Category category) {
//		return CategoryDTO.builder().id(category.getId()).name(category.getName()).build();
//	}
//
//	public static TherapistAvailabilitySlotDTO therapistsSlotToDto(TherapistAvailabilitySlot slot) {
//		return TherapistAvailabilitySlotDTO.builder()
//				.id(slot.getId())
//				.therapistId(slot.getTherapist().getId())
//				.date(slot.getDate())
//				.timeSlots(slot.getTimeSlots())
//				.build();
//	}
//
//	public static BookingDTO bookingToDto(Booking booking) {
//		TherapistInfoUserDto therapistInfo = TherapistInfoUserDto.builder()
//				.therapistId(booking.getTherapist().getId())
//				.fullname(booking.getTherapist().getFullname())
//				.build();
//		
//		return BookingDTO.builder()
//				.id(booking.getId())
//				.appointmentDateTime(booking.getAppointmentDateTime())
//				.cancellationDateTime(booking.getCancellationDateTime())
//				.minutes(booking.getMinutes())
//				.notes(booking.getNotes())
//				.timeSlot(booking.getTimeSlot())
//				.userData(booking.getUserData())
//				.therapistInfo(therapistInfo)
//				.bookingStatus(booking.getBookingStatus())
//				.paymentStatus(booking.getPaymentStatus())
//				.bookingType(booking.getBookingType())
//				.payment(booking.getPayment())
//				.build();
//	}
//
//	public static TherapistDto therapistToTherapistDtoMini(Therapist therapist) {
//		return TherapistDto.builder()
//				.id(therapist.getId())
//				.username(therapist.getUsername())
//				.fullname(therapist.getFullname())
//				.email(therapist.getEmail())
//				.mobile(therapist.getMobile())
//				.role(therapist.getRole().name())
//				.nonLocked(therapist.isNonLocked())
//				.enabled(therapist.isEnabled())
//				.activated(therapist.isActivated())
//				.submited(therapist.isSubmited())
//				.created(therapist.getCreated())
//				.imageUrl(therapist.getImageUrl())
////				.image(therapist.getImage() == null ? null
////						// : therapist.getImage()
////						: ImageUtils.decompress(therapist.getImage()))
////				.imageName(therapist.getImageName())
////				.imageType(therapist.getImageType())
//				.therapistInfoDto(therapist.getTherapistInfo() == null ? null
//						: therapistInfoToDTOClassMini(therapist.getTherapistInfo()))
//				.build();
//	}
//
//	public static TherapistInfoDTO therapistInfoToDTOClassMini(TherapistInfo info) {
//		return TherapistInfoDTO.builder()
//				.id(info.getId())
//				.bio(info.getBio())
//				.therapistId(info.getTherapist().getId())
//				.experienceYears(info.getExperienceYears())
//				.hourlyRate(info.getHourlyRate())
//				.isCertified(info.isCertified())
//				.qualification(info.getQualification())
//				.address(info.getAddress())
//				.languages(info.getLanguages())
//				.categories(info.getCategories().stream()
//						// .map(Category::getName) // for name only
//						.map(TherapistUtils2::categoryToCategoryDTO).collect(Collectors.toList()))
//				.build();
//	}
//}
