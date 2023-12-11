package com.ar.therapist.vendor.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ar.therapist.vendor.entity.Therapist;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TherapistsDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final Therapist therapist;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(therapist.getRole().name()));
	}

	@Override
	public String getPassword() {
		return therapist.getPassword();
	}

	@Override
	public String getUsername() {
//		return user.getEmail();
		return therapist.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return therapist.isNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return therapist.isEnabled();
	}

}
