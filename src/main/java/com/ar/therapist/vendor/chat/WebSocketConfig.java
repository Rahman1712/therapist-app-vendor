package com.ar.therapist.vendor.chat;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;
	
	@Value("${cors.set.allowed.origins}")
	private String[] CROSS_ORIGIN_URLS;
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// with sockjs
		registry
		.addEndpoint("/ws")
//		.setAllowedOrigins("http://localhost:5173")
		.setAllowedOrigins(CROSS_ORIGIN_URLS)
		.withSockJS()
		.setTaskScheduler(threadPoolTaskScheduler);
		
		// without sockjs
		//registry.addEndpoint("/ws-message").setAllowedOriginPatterns("*");
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.setApplicationDestinationPrefixes("/app");
		config.enableSimpleBroker("/topic")
		.setTaskScheduler(threadPoolTaskScheduler);
	}
	
	@EventListener(SessionSubscribeEvent.class)
	public void onWebSocketSessionsConnected(SessionSubscribeEvent event) {
		System.err.println(event);
	  Message<byte[]> eventMessage = event.getMessage();
	  String token = getAuthorizationToken(eventMessage);
	  System.err.println(token);
	  // Bearer xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
	  // do whatever you need with user, throw exception if user should not be connected
	  // ...
	}

	private String getAuthorizationToken(Message<byte[]> message) {
		System.err.println(message);
	  StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

	  Optional<StompHeaderAccessor> of = Optional.of(headerAccessor);
	  System.err.println(of);
	  
	  List<String> authorization = Optional.of(headerAccessor)
	      .map($ -> $.getNativeHeader(WebSocketHttpHeaders.AUTHORIZATION))
	      .orElse(Collections.emptyList());
	  // if header does not exists returns null instead empty list :/

	  return authorization.stream()
	      .findFirst()
	      .orElseThrow(() -> new IllegalArgumentException("Missing access token in Stomp message headers"));
	}
	
}
