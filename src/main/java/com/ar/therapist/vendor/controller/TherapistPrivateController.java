package com.ar.therapist.vendor.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ar.therapist.vendor.dto.TherapistDto;
import com.ar.therapist.vendor.dto.TherapistInfoRequest;
import com.ar.therapist.vendor.dto.UserUpdateRequest;
import com.ar.therapist.vendor.service.TherapistInfoService;
import com.ar.therapist.vendor.service.TherapistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/private")
@RequiredArgsConstructor 
public class TherapistPrivateController {

	private final TherapistService therapistService;
	private final TherapistInfoService therapistInfoService;
	
//	@PostMapping("/therapist-info/by-therapistid/{id}")
//	public ResponseEntity<?> therapistInfoById(@PathVariable("id")Long id, 
//			@RequestBody TherapistInfoRequest infoRequest){
//		//return	ResponseEntity.ok(therapistService.createTherapistInfoWithCategories(id, infoRequest));
//		return	ResponseEntity.ok(therapistService.addTherapistInfoById(infoRequest, id));
//	}
	
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<TherapistDto> getTherapistById(@PathVariable("id")Long id){
		return ResponseEntity.ok(therapistService.findById(id));
	}
	
	@PutMapping("/profile-image/byId/{id}")
	public ResponseEntity<TherapistDto> updateTherapistProfileImageById(@PathVariable("id")Long id,
			@RequestParam("image") MultipartFile image) throws IOException{
		return ResponseEntity.ok(therapistService.updateTherapistProfileImageById(id, image));
	}

	@PutMapping("/update/profile-data/byid/{therapistId}")
	public ResponseEntity<TherapistDto> updateProfileData(@PathVariable("therapistId") Long therapistId,@RequestBody UserUpdateRequest request){
		return ResponseEntity.ok(therapistService.updateProfileData(therapistId, request));
	}
	
	@PostMapping("/therapist-info/by-therapistid/{id}")
	public ResponseEntity<?> therapistInfoById(@PathVariable("id") Long id, 
	        @RequestPart TherapistInfoRequest infoRequest, // Use @RequestPart to handle file uploads
	        @RequestPart("educationalCertificate") MultipartFile educationalCertificate,
	        @RequestPart("experienceCertificate") MultipartFile experienceCertificate,
	        @RequestPart(name =  "additionalCertificate" , required = false) MultipartFile additionalCertificate) throws IOException {
	    return ResponseEntity.ok(therapistService.addTherapistInfoById(
	    		infoRequest, 
	    		educationalCertificate, 
	    		experienceCertificate, 
	    		additionalCertificate,  
	    		id));
	}
	
	@PutMapping("/update-categories/byid/{therapistInfoId}")
	public ResponseEntity<String> enableDisableById(@PathVariable("therapistInfoId") Long therapistInfoId, 
			@RequestBody List<String> categoryNames){
		
		therapistInfoService.updateCategoriesToTherapistInfo(therapistInfoId, categoryNames);
		return ResponseEntity.ok("Activated Successfully");
	}
	
	@PutMapping("/update-additional-doc/byId/{therapistInfoId}")
	public ResponseEntity<String> updateTherapistAdditionalDocByTherapistInfoId(@PathVariable("therapistInfoId")Long therapistInfoId,
			@RequestParam("file") MultipartFile file) throws IOException{
		therapistInfoService.updateTherapistAdditionalDocByTherapistInfoId(therapistInfoId, file);
		return ResponseEntity.ok("Additional Certificate Updated Successfully");
	}
	
	@PutMapping("/update-about/byId/{therapistInfoId}")
	public ResponseEntity<String> updateTherapistAboutByTherapistInfoId(
			@PathVariable("therapistInfoId")Long therapistInfoId,
			@RequestParam("about") String about
			){
		therapistInfoService.updateTherapistAboutByTherapistInfoId(therapistInfoId, about);
		return ResponseEntity.ok("About Updated Successfully");
	}
}
