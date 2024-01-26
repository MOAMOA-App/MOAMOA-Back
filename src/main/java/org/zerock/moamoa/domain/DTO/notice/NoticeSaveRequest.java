package org.zerock.moamoa.domain.DTO.notice;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.NoticeType;

@Data
public class NoticeSaveRequest {
    private User sender;
    private User receiver;
    private Boolean readOrNot = false;
    private NoticeType type;
    private Product reference;

    @Builder
    public NoticeSaveRequest(User sender, User receiver, NoticeType type, Product reference) {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.reference = reference;
    }
}
