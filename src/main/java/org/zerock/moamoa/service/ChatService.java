package org.zerock.moamoa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.entity.ChatMessage;
import org.zerock.moamoa.domain.entity.ChatRoom;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.ChatMessageRepository;
import org.zerock.moamoa.repository.ChatRoomRepository;

@Service
public class ChatService {
	private final ChatMessageRepository chatMessageRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ProductService productService;
	private final UserService userService;

	public ChatService(ChatMessageRepository chatMessageRepository, ChatRoomRepository chatRoomRepository,
		ProductService productService, UserService userService) {
		this.chatMessageRepository = chatMessageRepository;
		this.chatRoomRepository = chatRoomRepository;
		this.productService = productService;
		this.userService = userService;
	}

	public List<ChatRoom> roomFindAll() {
		return chatRoomRepository.findAll();
	}

	public List<ChatMessage> messageFindAll() {
		return chatMessageRepository.findAll();
	}

	public ChatRoom saveChatRoom(Long sellerId, Long userId, Long Prodect_id) {
		User seller = userService.findById(sellerId);
		User buyer = userService.findById(userId);
		Product product = productService.findById(Prodect_id);
		ChatRoom chatRoom = new ChatRoom();
		// chatRoom.setSellerId(seller);
		// chatRoom.setUserId(buyer);
		// chatRoom.setProductId(product);
		return chatRoomRepository.save(chatRoom);
	}

	public ChatMessage saveChatMessage(String message, Long chatRoomId, Long senderId) {
		User sender = userService.findById(senderId);
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new RuntimeException("chatRoom not found"));

		ChatMessage chatMessage = new ChatMessage();
		// chatMessage.setMessage(message);
		// chatMessage.setSender(sender);
		// chatMessage.setChatRoom(chatRoom);
		return chatMessageRepository.save(chatMessage);
	}

}
