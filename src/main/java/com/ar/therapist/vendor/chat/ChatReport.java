package com.ar.therapist.vendor.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chats_reports")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatReport {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	private Chat chatMessage;
	private String reason;
}
