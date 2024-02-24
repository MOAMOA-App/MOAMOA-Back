package org.zerock.moamoa.domain.DTO.chat;

import lombok.Data;
import org.zerock.moamoa.domain.DTO.product.ProductMapper;
import org.zerock.moamoa.domain.DTO.product.ProductTitleResponse;
import org.zerock.moamoa.domain.DTO.user.UserResponse;
import org.zerock.moamoa.domain.entity.ChatRoom;

import java.util.List;

@Data
public class ChatRoomResponse {
    private Long id;
    private ProductTitleResponse product;
    private UserResponse seller;
    private UserResponse user;
    private List<ChatMessageResponse> messages;

    private String roomName;
    private String clientMsg;

    public static ChatRoomResponse fromEntity(ChatRoom chatRoom) {
        ChatRoomResponse response = new ChatRoomResponse();
        response.id = chatRoom.getId();
        response.product = ProductMapper.INSTANCE.toTitleDto(chatRoom.getProduct());
        response.seller = UserResponse.toDTO(chatRoom.getSeller());
        response.user = UserResponse.toDTO(chatRoom.getUser());
        response.roomName = chatRoom.getProduct().getTitle();
        return response;
    }

    public static ChatRoomResponse toClient(String msg) {
        ChatRoomResponse res = new ChatRoomResponse();
        res.clientMsg = msg;
        return res;
    }
}
