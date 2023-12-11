package com.ar.therapist.vendor.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	private final LogoutHandler logoutHandler;
	
	@Value("${cors.set.allowed.origins}")
	private String[] CROSS_ORIGIN_URLS;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
        .cors(Customizer.withDefaults())
		.csrf(csrf -> csrf.disable())
		.formLogin(formLogin -> formLogin.disable())
        .httpBasic(httpBasic -> httpBasic.disable())
        .exceptionHandling((exceptionHandling) ->
			exceptionHandling
				.authenticationEntryPoint(new RestAuthenticationEntryPoint())
				//.accessDeniedPage("/errors/access-denied")
		)
		.authorizeHttpRequests(ahr ->
			ahr.requestMatchers(
					"/api/v1/auth/**", 
					"/api/v1/public/**",
					"/api/v1/bookings/**",
					"/api/v1/payments/**",
					"/api/v1/reviews/**",
					"/api/v1/public-to-admin/**",
					"/api/v1/therapist-to-user/**",
					"/api/v1/therapist-to-admin/**",
					"/api/v1/therapist_booking/**",
					"/api/v1/messages-to-user/**",
					"/api/v1/therapist-availability-slots/**",
					"/api/v1/admin-defined-times/**",
					"/pckart/api/v1/user-to-admin/user/**",
					"/pckart/api/v1/user-to-admin/coupon/**",
					"/pckart/api/v1/user-to-admin/order/**",
					"/api/v1/video/**",
					"/ws/**",
					
					"/resources/**",
					"/static/**",
					"/css/**",
					"/js/**",
					"/images/**",
					"/vendor/**",
					"/fonts/**"
			).permitAll()
			.anyRequest().authenticated()
		)
		.sessionManagement(sm -> 
		     sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    )
		.authenticationProvider(authenticationProvider)
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
		.logout((logout) -> logout
			.logoutUrl("/api/v1/auth/logout")
			.addLogoutHandler(logoutHandler)
			.logoutSuccessHandler((request, response, authentication) -> 
				SecurityContextHolder.clearContext()
			)
 		)
		;

    return http.build();
	}
    
//	@Bean
//	public CorsFilter corsFilter() {
//		CorsConfiguration corsConfiguration = new CorsConfiguration();
//		corsConfiguration.setAllowCredentials(true);
//		corsConfiguration.setAllowedOrigins(Arrays.asList(CROSS_ORIGIN_URLS));
//		corsConfiguration.setAllowedHeaders(Arrays.asList(
//					"Origin","Access-Control-Allow-Origin", "Content-Type",
//					"Accept","Authorization", "Origin, Accept","X-Requested-With",
//					"Access-Control-Request-Method","Access-Control-Request-Headers",
//					"Stripe-Token", "Stripe-Amount"
//				));
//		corsConfiguration.setExposedHeaders(Arrays.asList(
//					"Origin","Content-Type","Accept","Authorization",
//					"Access-Control-Allow-Origin","Access-Control-Allow-Origin",
//					"Access-Control-Allow-Credentials"
//				));
//		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
//		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//		return new CorsFilter(urlBasedCorsConfigurationSource);
//	}
	
	
	
	
}

/*
	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList(CROSS_ORIGIN_URLS));
		corsConfiguration.setAllowedHeaders(Arrays.asList(
					"Origin","Access-Control-Allow-Origin", "Content-Type",
					"Accept","Authorization","Origin, Accept","X-Requested-With",
					"Access-Control-Request-Method","Access-Control-Request-Headers"
				));
		corsConfiguration.setExposedHeaders(Arrays.asList(
					"Origin","Content-Type","Accept","Authorization",
					"Access-Control-Allow-Origin","Access-Control-Allow-Origin",
					"Access-Control-Allow-Credentials"
				));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}
	
	@Bean
	public CorsFilter corsFilter() {
	    CorsConfiguration corsConfiguration = new CorsConfiguration();
	    corsConfiguration.setAllowCredentials(true);
	    corsConfiguration.setAllowedOrigins(Arrays.asList(CROSS_ORIGIN_URLS));
	    corsConfiguration.setAllowedHeaders(Arrays.asList(
	        "Origin", "Access-Control-Allow-Origin", "Content-Type", "Accept", "Authorization"
	    ));
	    corsConfiguration.setExposedHeaders(Arrays.asList(
	        "Origin", "Content-Type", "Accept", "Authorization", "Access-Control-Allow-Origin",
	        "Access-Control-Allow-Credentials"
	    ));
	    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", corsConfiguration);

	    // Handle preflight requests by allowing specific methods and headers
	    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	    corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
	    corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));

	    // Add an OPTIONS preflight request handler
	    source.registerCorsConfiguration("/**", corsConfiguration);
	    return new CorsFilter(source);
	}

 */
