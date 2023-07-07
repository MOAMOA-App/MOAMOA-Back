package org.zerock.moamoa.domain.DTO.notice;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.DTO.user.UserResponse;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

import java.time.Instant;

@Data
public class NoticeResponse {
    private Long id;
    private User senderID;
    private User receiverID;
    private String message;
    private Boolean readOrNot;
    private String type;
    private Product referenceID;
    private Instant createdAt;

    public String getSenderNickname() { // User 닉네임 가져옴
        return senderID.getNick();
    }

    public String getReferenceTitle(){
        return referenceID.getTitle();  // Product 제목 가져옴
    }

    @Builder
    public NoticeResponse(Long id, User senderID, User receiverID, String message, Boolean readOrNot,
                          String type, Product referenceID, Instant createdAt) {
        this.id = id;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.message = message;
        this.readOrNot = readOrNot;
        this.type = type;
        this.referenceID = referenceID;
        this.createdAt = createdAt;
    }
}
