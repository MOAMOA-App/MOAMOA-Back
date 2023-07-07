package org.zerock.moamoa.domain.DTO.notice;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

import java.time.Instant;

@Data
public class NoticeSaveRequest {
    private Long id;

    private User senderID;

    private User receiverID;

    private String message;

    private Boolean readOrNot = false;

    private String type;

    private Product referenceID;

    private Instant createdAt;

    @Builder
    public NoticeSaveRequest(Long id, User senderID, User receiverID, String message, Boolean readOrNot,
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
