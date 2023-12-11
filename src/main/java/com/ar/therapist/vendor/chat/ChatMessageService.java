package com.ar.therapist.vendor.chat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ar.therapist.vendor.dto.TherapistInfoUserDto;
import com.ar.therapist.vendor.dto.TherapistUserDto;
import com.ar.therapist.vendor.entity.TherapistInfo;
import com.ar.therapist.vendor.entity.UserData;
import com.ar.therapist.vendor.exception.ChatException;
import com.ar.therapist.vendor.service.TherapistService;
import com.ar.therapist.vendor.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

	private final ChatRepository chatRepository;
	private final TherapistService therapistService;
	private final UserService userService;
	private final MessageRepository messageRepository;
	
	public Chat createChat(ChatRequest chatRequest) {
		Optional<Chat> chatOptional = chatRepository.findByRoomId(chatRequest.getRoomId());
		
		if(chatOptional.isPresent()) {
			return chatOptional.get();
		}
		
		Chat chat = new Chat();
		chat.setUserId(chatRequest.getUserId());
		chat.setTherapistId(chatRequest.getTherapistId());
		chat.setRoomId(chatRequest.getRoomId());
		
		return chatRepository.save(chat);
	}
	
	public void deleteById(Long chatId) {
		chatRepository.deleteById(chatId);
	}
	
	public Chat findChatById(Long chatId){
		Optional<Chat> chat = chatRepository.findById(chatId);
		if(chat.isPresent()) {
			return chat.get();
		}
		throw new ChatException("Chat not found");
	}
	
	public Chat findByRoomId(String roomId){
		return chatRepository.findByRoomId(roomId).orElse(null);
	}

//	public List<Chat> findAllChatByUserId(Long userId) {
//		List<Chat> chats = chatRepository.findByUserId(userId);
//		return chats;
//	}
//	
//	public List<Chat> findAllChatByTherapistId(Long therapistId) {
//		List<Chat> chats = chatRepository.findByTherapistId(therapistId);
//		return chats;
//	}
	
	public List<TherapistInfoUserDto> findTherapistsListByUserId(Long userId){
		List<Long> findTherapistIdsByUserId = chatRepository.findTherapistIdsByUserId(userId);
		return therapistService.getTherapistInfoUserByIds(findTherapistIdsByUserId);
	}
	
	public List<UserData> findUsersListByTherapistId(Long therpistId){
		List<Long> userIds = chatRepository.findUserIdsByTherapistId(therpistId);
		return userService.findUserDatasByUserIds(userIds);
	}
	
//	===================================== Message
	
	public Message saveMessage(String roomId, MessageRequest messageRequest) {
		Chat chat = chatRepository.findByRoomId(roomId)
				.orElseThrow(() ->  new ChatException("Chat not fount with room id "+ roomId));
		
		Message message = Message.builder()
				.chat(chat)
				.content(messageRequest.getContent())
				.timestamp(LocalDateTime.now())
				.role(messageRequest.getRole())
				.build();
		
		return messageRepository.save(message);
	}
	
}
