package org.zerock.moamoa.domain.DTO.notice;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

import java.time.Instant;

@Data
public class NoticeSaveRequest {
    private Long senderID;
    private Long receiverID;
    private String message;
    private Boolean readOrNot = false;
    private String type;
    private Long referenceID;

    @Builder
    public NoticeSaveRequest(String message, String type) {
        this.message = message;
        this.type = type;
    }
}
