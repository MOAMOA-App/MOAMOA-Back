package org.zerock.moamoa.domain.DTO.notice;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.domain.DTO.user.UserProfileResponse;
import org.zerock.moamoa.domain.DTO.user.UserResponse;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.NoticeType;

import java.time.Instant;

@Data
public class NoticeResponse {
    private Long id;
    private UserProfileResponse senderID;
    private UserProfileResponse receiverID;
    private Boolean readOrNot;
    private NoticeType type;
    private ProductResponse referenceID;
    private Instant createdAt;

    @Builder
    public NoticeResponse(Long id, UserProfileResponse senderID, UserProfileResponse receiverID, Boolean readOrNot,
                          NoticeType type, ProductResponse referenceID, Instant createdAt) {
        this.id = id;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.readOrNot = readOrNot;
        this.type = type;
        this.referenceID = referenceID;
        this.createdAt = createdAt;
    }
}
