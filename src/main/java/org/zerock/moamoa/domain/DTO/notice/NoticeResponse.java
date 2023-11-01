package org.zerock.moamoa.domain.DTO.notice;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.domain.DTO.product.ProductMapper;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.domain.DTO.user.UserProfileResponse;
import org.zerock.moamoa.domain.DTO.user.UserResponse;
import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.NoticeType;
import org.zerock.moamoa.utils.TimeUtils;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NoticeResponse {
    private Long id;
    private UserProfileResponse senderID;
    private UserProfileResponse receiverID;
    private Boolean readOrNot;
    private NoticeType type;
    private ProductResponse referenceID;
    private LocalDateTime createdAt;

    @Builder
    public NoticeResponse(Long id, UserProfileResponse senderID, UserProfileResponse receiverID, Boolean readOrNot,
                          NoticeType type, ProductResponse referenceID, Instant createdAt) {
        this.id = id;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.readOrNot = readOrNot;
        this.type = type;
        this.referenceID = referenceID;
        this.createdAt = TimeUtils.toLocalTime(createdAt);
    }

    public static NoticeResponse toDto(Notice notice) {
        NoticeResponse res = new NoticeResponse();
        res.id = notice.getId();
        res.senderID = UserProfileResponse.builder(notice.getSenderID());
        res.receiverID = UserProfileResponse.builder(notice.getReceiverID());
        res.readOrNot = notice.getReadOrNot();
        res.type = notice.getType();
        res.referenceID = ProductMapper.INSTANCE.toDto(notice.getReferenceID());
        res.createdAt = TimeUtils.toLocalTime(notice.getCreatedAt());
        return res;
    }
}
