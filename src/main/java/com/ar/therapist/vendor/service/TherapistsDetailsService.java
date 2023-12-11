package com.ar.therapist.vendor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ar.therapist.vendor.repo.TherapistRepository;

public class TherapistsDetailsService implements UserDetailsService {

	@Autowired
	private TherapistRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findByUsername(username).map(TherapistsDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not Found"));
	}

}
