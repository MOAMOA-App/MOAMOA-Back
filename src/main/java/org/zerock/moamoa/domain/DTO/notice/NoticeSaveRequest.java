package org.zerock.moamoa.domain.DTO.notice;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.NoticeType;

import java.time.Instant;

@Data
public class NoticeSaveRequest {
    private Long senderID;
    private Long receiverID;
    private Boolean readOrNot = false;
    private NoticeType type;
    private Long referenceID;

    @Builder
    public NoticeSaveRequest(Long senderID, Long receiverID, NoticeType type, Long referenceID) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.type = type;
        this.referenceID = referenceID;
    }
}
