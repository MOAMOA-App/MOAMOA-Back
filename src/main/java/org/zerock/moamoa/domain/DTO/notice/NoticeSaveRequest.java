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
    public NoticeSaveRequest(NoticeType type) {
        this.type = type;
    }
}
