package com.ar.therapist.vendor.chat;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageRequest {

	private String roomId;
	private Long chatId;
	private String content;
	private LocalDateTime timestamp;
	private MessageRole role;
}