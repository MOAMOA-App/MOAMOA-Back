package org.zerock.moamoa.domain.DTO.notice;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.domain.DTO.product.ProductMapper;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.domain.DTO.user.UserProfileResponse;
import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.enums.NoticeType;
import org.zerock.moamoa.utils.TimeUtils;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NoticeContentResponse {
    private Long id;
    private Long senderID;  // YJ: userproductresponse로 고치기
    private Long receiverID;
    private Boolean readOrNot;
    private NoticeType type;
    private Long referenceID;
    private LocalDateTime createdAt;

    @Builder
    public NoticeContentResponse(Long id, Long senderID, Long receiverID, Boolean readOrNot,
                                 NoticeType type, Long referenceID, Instant createdAt) {
        this.id = id;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.readOrNot = readOrNot;
        this.type = type;
        this.referenceID = referenceID;
        this.createdAt = TimeUtils.toLocalTime(createdAt);
    }

    public static NoticeContentResponse toDto(Notice notice) {
        NoticeContentResponse res = new NoticeContentResponse();
        res.id = notice.getId();
        res.senderID = notice.getSenderID().getId();
        res.receiverID = notice.getReceiverID().getId();
        res.readOrNot = notice.getReadOrNot();
        res.type = notice.getType();
        res.referenceID = notice.getReferenceID().getId();
        res.createdAt = TimeUtils.toLocalTime(notice.getCreatedAt());
        return res;
    }
}
