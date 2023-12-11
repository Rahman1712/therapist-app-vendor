package com.ar.therapist.vendor.service;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ar.therapist.vendor.config.JwtService;
import com.ar.therapist.vendor.dto.AuthenticationRequest;
import com.ar.therapist.vendor.dto.AuthenticationResponse;
import com.ar.therapist.vendor.dto.TherapistRegisterRequest;
import com.ar.therapist.vendor.entity.Role;
import com.ar.therapist.vendor.entity.Therapist;
import com.ar.therapist.vendor.entity.Token;
import com.ar.therapist.vendor.entity.TokenType;
import com.ar.therapist.vendor.exception.ErrorResponse;
import com.ar.therapist.vendor.exception.TherapistException;
import com.ar.therapist.vendor.exception.TherapistRegisterException;
import com.ar.therapist.vendor.repo.TherapistRepository;
import com.ar.therapist.vendor.repo.TokenRepository;
import com.ar.therapist.vendor.utils.CookieUtils;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class TherapistsAuthenticateService {

	@Autowired private TherapistRepository therapistRepository;
	@Autowired private JwtService jwtService;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private AuthenticationManager authenticationManager;
	@Autowired private MailService mailService;
	@Autowired private OTPService otpService;
	@Autowired private TokenRepository tokenRepo; 
	
	// register
	public AuthenticationResponse register(TherapistRegisterRequest request) {
			var therapist = Therapist.builder()
					.fullname(request.getFullname())
					.mobile(request.getMobile())
					.email(request.getEmail())
					.username(request.getUsername())
					.password(passwordEncoder.encode(request.getPassword()))
					.role(Role.THERAPIST)
					.nonLocked(true) // at register not lock true
					.enabled(false) // not enable at start
					.activated(false) // not activated 
					.submited(false) // false
					.created(LocalDateTime.now())
					.build();
			
			therapistRepository.findByUsername(request.getUsername()).ifPresent(
					u-> {
						throw new TherapistRegisterException(new ErrorResponse("username already exists.", "username", request.getUsername()));
					});
			therapistRepository.findByEmail(request.getEmail()).ifPresent(
					u-> {
						throw new TherapistRegisterException(new ErrorResponse("email already exists.", "email", request.getEmail()));
					});
			
			Therapist therpaistSaved = therapistRepository.save(therapist);
			
			try {
				sendMailForVerify(therpaistSaved);
			} catch (UnsupportedEncodingException | MessagingException e) {
				e.printStackTrace();
				throw new TherapistException("Error in new Registration ...");
			}
			
			//var jwtToken = jwtService.generateToken(new TherapistsDetails(therpaistSaved));
			//var refreshToken = jwtService.generateRefreshToken(new TherapistsDetails(therpaistSaved));
			//saveUserToken(therpaistSaved, jwtToken);
			
			
			return AuthenticationResponse.builder()
					.username(therpaistSaved.getUsername())
					.id(therpaistSaved.getId())
					.accessToken("jwtToken")
					//.refreshToken(refreshToken)
					.build(); 
	}

	// login
	public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletResponse response) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						 request.getUsername(),
						 request.getPassword()
						 )
				);
		
		var therapist = therapistRepository.findByUsername(request.getUsername())
				.orElseThrow();
		var jwtToken = jwtService.generateToken(new TherapistsDetails(therapist));
		var refreshToken = jwtService.generateRefreshToken(new TherapistsDetails(therapist));
		
		CookieUtils.addCookie(response, "refresh_token", refreshToken, 7 * 24 * 3600);
		
		revokeAllUserTokens(therapist);
		saveUserToken(therapist, jwtToken);
		
		return AuthenticationResponse.builder()
				.id(therapist.getId())
				.accessToken(jwtToken)
				//.refreshToken(refreshToken)
				.username(therapist.getUsername())
				.fullname(therapist.getFullname())
				.email(therapist.getEmail())
				.role(therapist.getRole().name())
				.mobile(therapist.getMobile())
				.imageUrl(therapist.getImageUrl())
				.submited(therapist.isSubmited())
				.build();
	}
	
	private void saveUserToken(Therapist therapist, String jwtToken) {
		var token = Token.builder()
				.therapist(therapist)
				.token(jwtToken)
				.tokenType(TokenType.BEARER)
				.expired(false)
				.revoked(false)
				.build();
		tokenRepo.save(token);
	}
	
	private void revokeAllUserTokens(Therapist therapist) {
		var validTherapistTokens = tokenRepo.findAllValidTokensByTherapist(therapist.getId());
		
		if(validTherapistTokens.isEmpty()) return;
		
		validTherapistTokens.forEach(t ->{
			t.setExpired(true);
			t.setRevoked(true);
		});
		
		tokenRepo.saveAll(validTherapistTokens);
		
		tokenRepo.deleteAll(validTherapistTokens);
	}
	
	// refresh tokens
	public void refreshToken(
			HttpServletRequest request, 
			HttpServletResponse response) 
		throws StreamWriteException, DatabindException, IOException {
		
		final String refreshToken;
		final String userName;
		Optional<Cookie> cookie = CookieUtils.getCookie(request, "refresh_token");
		if(cookie.isEmpty()) {
			//return;
			throw new TherapistException("Your Session has been Expired Please Login");
		}
		refreshToken = cookie.get().getValue();
		try {
			userName = jwtService.extractUsername(refreshToken);
		
			if(userName != null) {
				var therapist = this.therapistRepository.findByUsername(userName)
						.orElseThrow();
				
				if(jwtService.isTokenValid(refreshToken, new TherapistsDetails(therapist))) {
					var accessToken = jwtService.generateToken(new TherapistsDetails(therapist));
					
					revokeAllUserTokens(therapist);  /// 
					saveUserToken(therapist, accessToken);  /// 
					
					var authResponse = AuthenticationResponse.builder()
							.accessToken(accessToken)
							//.refreshToken(refreshToken)
							.build();
					new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
				}
			}
		}catch(ExpiredJwtException ex) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
			
			new ObjectMapper().writeValue(response.getOutputStream(), "Session has been Expired");
		}catch(Exception ex) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			httpResponse.getWriter().write("An Error occured");
		}
	}

	// send mail function therapist
	public String sendMailForVerify(Therapist therapist) throws UnsupportedEncodingException, MessagingException {
    	String otp = otpService.generateOTP(therapist);
    	System.out.println("OTP : "+otp);
    	return mailService.sendOTPMail(therapist, otp);
//    	return "OTP sended to ✉️ : "+therapist.getEmail();
	}
	
	// send mail function email -> send mail abow abow function
	public String sendMailForVerify(String userEmail) throws UnsupportedEncodingException, MessagingException {
    	Therapist therapist = therapistRepository.findByEmail(userEmail)
        		.orElseThrow(() -> new TherapistException("Not Valid Email Id"));
    	return sendMailForVerify(therapist);	
	}
	
	// verify otp -- this at signup time
	public AuthenticationResponse verifyOtp(String email, String otp, HttpServletResponse response) {
		var therapist = this.therapistRepository.findByEmail(email)
				.orElseThrow(() -> new TherapistException("Not Valid Username"));
		
		if(otpService.verifyOTP(therapist, otp)) {
			therapistRepository.updateEnabledById(therapist.getId(), true); //TRUE  : also update to enabled to true  
		}
		else {
			throw new TherapistException("Invalid otp : OTP is incorrect ❌");
		}
		
		System.out.println("OTP verified successfully ✅.");//"OTP verified successfully ✅. Please login to continue."
		var jwtToken = jwtService.generateToken(new TherapistsDetails(therapist));
		var refreshToken = jwtService.generateRefreshToken(new TherapistsDetails(therapist));

		CookieUtils.addCookie(response, "refresh_token", refreshToken, 7 * 24 * 3600);
		
		revokeAllUserTokens(therapist);
		saveUserToken(therapist, jwtToken);
		
		return AuthenticationResponse.builder()
				.accessToken(jwtToken)
				.id(therapist.getId())
				.username(therapist.getUsername())
				.fullname(therapist.getFullname())
				.email(therapist.getEmail())
				.role(therapist.getRole().name())
				.mobile(therapist.getMobile())
				.imageUrl(therapist.getImageUrl())
				.submited(therapist.isSubmited())
				.build();
	}
	
	// verify forgot otp and : return token
	public String verifyForgotOtp(String email, String otp) {
		var therapist = this.therapistRepository.findByEmail(email)
				.orElseThrow(() -> new TherapistException("Not Valid Username"));
		
		if(otpService.verifyOTP(therapist, otp)) {
			therapistRepository.updateEnabledById(therapist.getId(), true); //TRUE  : also update to enabled to true  
		}
		else {
			throw new TherapistException("Invalid otp : OTP is incorrect ❌");
		}
		
		var jwtToken = jwtService.generateToken(new TherapistsDetails(therapist));
		
		return jwtToken; //"OTP verified successfully ✅."
	}
	
	// refresh otp
	public String refreshOtp(String email) {
		Therapist therapist = this.therapistRepository.findByEmail(email)
				.orElseThrow(() -> new TherapistException("Not a Valid therapist")); 
		
		try {
			sendMailForVerify(therapist);
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
			throw new TherapistException("Error in Otp Resend");
		}
		
		return "OTP Resended to ✉️ : "+therapist.getEmail();
	}
	
	// update forgot password
	public String updateForgotPassword(String username, String newPassword, String token) {
		Therapist therapist = therapistRepository.findByUsername(username)
        		.orElseThrow(() -> new TherapistException("Not Valid Username ❌"));  
		
		if(!jwtService.isTokenValid(token, new TherapistsDetails(therapist))) {
			throw new TherapistException("Not Valid Token ❌");
		}
		
		therapistRepository.updatePassword(therapist.getId(), passwordEncoder.encode(newPassword));
		return "Password updated successfully ✅"; 
	}
	
	
}
