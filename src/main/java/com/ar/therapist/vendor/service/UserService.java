package com.ar.therapist.vendor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.ar.therapist.vendor.common.CommonJwtService;
import com.ar.therapist.vendor.entity.UserData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
//	private final WebClient webClient;
	private final CommonJwtService commonJwtService;
	private final ReactorLoadBalancerExchangeFilterFunction lbFunction;
//	private final WebClient.Builder loadBalancedWebClientBuilder;
	public WebClient.Builder loadBalancedWebClientBuilder() {
	    return WebClient.builder();
	}
	
	
//	@Autowired
//	private ReactorLoadBalancerExchangeFilterFunction lbFunction;
//	@Bean
//	public WebClient.Builder loadBalancedWebClientBuilder() {
//	    return WebClient.builder();
//	}

	@Value("${user.service.api.url.users_by_ids}")
	private String USER_SERVICE_URL_USERS_BYIDS;
	
	public List<UserData> findUserDatasByUserIds(List<Long> userIds) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromUriString(USER_SERVICE_URL_USERS_BYIDS)
				.queryParam("userIds", userIds);
		String uriPathWithQueryParams = uriBuilder.toUriString();
		
		List<UserData> userDatas = 
				loadBalancedWebClientBuilder().filter(lbFunction).build()
				.get()
				.uri(uriPathWithQueryParams)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+commonJwtService.generateToken(getAuthenticationUsername()))
				.header("Username", getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, response-> response.createError())
				.bodyToFlux(UserData.class)
				.collectList()
				.block()
				;
		  
		  return userDatas;
	}
	
	private String getAuthenticationUsername() {
		Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		return authentication.getName();
	}
	
}
