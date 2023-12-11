package com.ar.therapist.vendor.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.therapist.vendor.entity.UserData;
import com.ar.therapist.vendor.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserServiceController {

	private final UserService userService; 
	
	@GetMapping("/users-byids")
	public ResponseEntity<List<UserData>> getUserDatasByIds(@RequestParam("userIds") List<Long> userIds){
		return ResponseEntity.ok(userService.findUserDatasByUserIds(userIds));
	}
}
