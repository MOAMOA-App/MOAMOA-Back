package org.zerock.moamoa.domain.DTO.chat;

import lombok.Data;
import org.zerock.moamoa.domain.DTO.product.ProductMapper;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.domain.DTO.user.UserProfileResponse;
import org.zerock.moamoa.domain.entity.ChatMessage;
import org.zerock.moamoa.domain.entity.ChatRoom;

import java.util.List;

@Data
public class ChatRoomResponse {
    private Long id;

    private ProductResponse productId;

    private UserProfileResponse sellerId;

    private UserProfileResponse userId;

    private List<ChatMessage> messages;
    private String roomName;

    public static ChatRoomResponse fromEntity(ChatRoom chatRoom) {
        ChatRoomResponse response = new ChatRoomResponse();
        response.id = chatRoom.getId();
        response.productId = ProductMapper.INSTANCE.toDto(chatRoom.getProductId());
        response.sellerId = UserProfileResponse.builder(chatRoom.getSellerId());
        response.userId = UserProfileResponse.builder(chatRoom.getUserId());
        response.roomName = "test";
        return response;
    }
}
