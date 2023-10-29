package org.zerock.moamoa.domain.DTO.notice;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.domain.DTO.product.ProductMapper;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.domain.DTO.user.UserProfileResponse;
import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.enums.NoticeType;

@Data
@NoArgsConstructor
public class NoticeContentResponse {
    private Long id;
    private Long senderID;
    private Long receiverID;
    private Boolean readOrNot;
    private NoticeType type;
    private Long referenceID;
    private String createdAt;

    public static NoticeContentResponse toDto(Notice notice) {
        NoticeContentResponse res = new NoticeContentResponse();
        res.id = notice.getId();
        res.senderID = notice.getSenderID().getId();
        res.receiverID = notice.getReceiverID().getId();
        res.readOrNot = notice.getReadOrNot();
        res.type = notice.getType();
        res.referenceID = notice.getReferenceID().getId();
        res.createdAt = notice.getCreatedAt().toString();
        return res;
    }
}
