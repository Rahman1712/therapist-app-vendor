package com.ar.therapist.vendor.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRequest {

	private Long userId;
	private Long therapistId;
	private String roomId;
}
