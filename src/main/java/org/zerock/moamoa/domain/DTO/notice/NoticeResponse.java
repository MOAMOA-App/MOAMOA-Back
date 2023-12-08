package org.zerock.moamoa.domain.DTO.notice;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.domain.DTO.product.ProductTitleResponse;
import org.zerock.moamoa.domain.DTO.user.UserNickResponse;
import org.zerock.moamoa.domain.enums.NoticeType;
import org.zerock.moamoa.utils.TimeUtils;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NoticeResponse {
    private Long id;
    private UserNickResponse senderID;
    private Long receiverID;
    private Boolean readOrNot;
    private NoticeType type;
    private ProductTitleResponse referenceID;
    private LocalDateTime createdAt;

    @Builder
    public NoticeResponse(Long id, UserNickResponse senderID, Long receiverID, Boolean readOrNot,
                          NoticeType type, ProductTitleResponse referenceID, Instant createdAt) {
        this.id = id;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.readOrNot = readOrNot;
        this.type = type;
        this.referenceID = referenceID;
        this.createdAt = TimeUtils.toLocalTime(createdAt);
    }
}
