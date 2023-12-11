package com.ar.therapist.vendor.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ar.therapist.vendor.entity.Therapist;
import com.ar.therapist.vendor.exception.TherapistException;
import com.ar.therapist.vendor.repo.TherapistRepository;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider   {
	
	@Autowired
	private TherapistRepository repo;
	
	@Autowired
	private PasswordEncoder encoder;  
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		System.err.println("CCCCCCCCCCCCCCC");
		String username = authentication.getName();
	    String password = authentication.getCredentials().toString();
	    Therapist therapist = repo.findByUsername(username).orElseThrow(()->
				new TherapistException("username not found"));
	    if (!encoder.matches(password, therapist.getPassword())) {
	    	throw new TherapistException("Invalid Password");
	    }
	    if(!therapist.isEnabled()) throw new TherapistException("account is not enabled");
	    if(!therapist.isNonLocked()) throw new TherapistException("account is locked");
	    	
	    List<String> userRoles = List.of(therapist.getRole().name());
	    
	    return new UsernamePasswordAuthenticationToken(username, password, userRoles.stream().map(x -> new SimpleGrantedAuthority(x)).collect(Collectors.toList()));
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
}

/*
String username = authentication.getPrincipal() + "";
if (admin == null) throw new BadCredentialsException("Invalid Username");
new UsernameNotFoundException("username not found"));
throw new BadCredentialsException("Invalid Password");

if (admin.isDisabled()) {
    throw new DisabledException("");
    throw new AdminLoginException("account disabled");
}
if (admin.isLocked()) {
    throw new LockedException("");
    throw new AdminLoginException("account locked");
}
 
	    Admin admin = repo.findByUsername(username).get();
 List<Role> userRoles = rolesRepo.getUserRoles(username); // roles from roles table
	    return new UsernamePasswordAuthenticationToken(username, null, userRoles.stream().map(x -> new SimpleGrantedAuthority(x.getName())).collect(Collectors.toList()));
	    return new UsernamePasswordAuthenticationToken(username, null, userRoles.stream().map(x -> new SimpleGrantedAuthority(x)).collect(Collectors.toList()));
*/