package com.ar.therapist.vendor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ar.therapist.vendor.dto.TherapistDto;
import com.ar.therapist.vendor.service.TherapistService;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {
	
	@Autowired private TherapistService therapistService;

	@GetMapping
	public String worked() {
		System.err.println("DEMOOOOOOOO");
		return "Hey its a private data 📁 🔐"; 
	}
	
	@GetMapping("/users")
    public ResponseEntity<List<TherapistDto>> allUsers(){
    	return ResponseEntity.ok(therapistService.findAll());
    }
}
