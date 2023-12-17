package org.zerock.moamoa.domain.DTO.chat;

import lombok.Data;
import org.zerock.moamoa.domain.DTO.product.ProductMapper;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.domain.DTO.user.UserResponse;
import org.zerock.moamoa.domain.entity.ChatRoom;

import java.util.List;

@Data
public class ChatRoomResponse {
    private Long id;
    private ProductResponse productId;
    private UserResponse sellerId;
    private UserResponse userId;
    private List<ChatMessageResponse> messages;

    private String roomName;
    private String clientMsg;

    public static ChatRoomResponse fromEntity(ChatRoom chatRoom) {
        ChatRoomResponse response = new ChatRoomResponse();
        response.id = chatRoom.getId();
        response.productId = ProductMapper.INSTANCE.toDto(chatRoom.getProductId());
        response.sellerId = UserResponse.builder(chatRoom.getSellerId());
        response.userId = UserResponse.builder(chatRoom.getUserId());
        response.roomName = chatRoom.getProductId().getTitle();
        return response;
    }

    public static ChatRoomResponse toClient(String msg) {
        ChatRoomResponse res = new ChatRoomResponse();
        res.clientMsg = msg;
        return res;
    }
}
