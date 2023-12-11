package com.ar.therapist.vendor.chat;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
	private Long therapistId;
    private Long userId;
    private String roomId;
    
	@OneToMany(mappedBy = "chat")
	private List<Message> messages;
    
}

// USER - THERAPIST CHATS

//private String message;
//private LocalDateTime timestamp;
//private String role; // "therapist" or "user"


/*
@Embedded
	private TherapistChatData therapistChatData;
	@Embedded
    private UserChatData userChatData;
*/