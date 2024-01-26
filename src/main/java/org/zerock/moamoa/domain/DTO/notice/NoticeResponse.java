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
    private UserNickResponse sender;
    private String receiver;
    private Boolean readOrNot;
    private NoticeType type;
    private ProductTitleResponse reference;
    private LocalDateTime createdAt;

    @Builder
    public NoticeResponse(Long id, UserNickResponse sender, String receiver, Boolean readOrNot,
                          NoticeType type, ProductTitleResponse reference, Instant createdAt) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.readOrNot = readOrNot;
        this.type = type;
        this.reference = reference;
        this.createdAt = TimeUtils.toLocalTime(createdAt);
    }
}
