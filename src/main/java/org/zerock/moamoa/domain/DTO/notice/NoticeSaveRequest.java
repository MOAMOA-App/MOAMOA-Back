package org.zerock.moamoa.domain.DTO.notice;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.NoticeType;

import java.time.Instant;

@Data
public class NoticeSaveRequest {
    private User senderID;
    private User receiverID;
    private Boolean readOrNot = false;
    private NoticeType type;
    private Product referenceID;

    @Builder
    public NoticeSaveRequest(User senderID, User receiverID, NoticeType type, Product referenceID) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.type = type;
        this.referenceID = referenceID;
    }
}
