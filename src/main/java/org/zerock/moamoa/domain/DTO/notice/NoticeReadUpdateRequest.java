package org.zerock.moamoa.domain.DTO.notice;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.entity.User;

@Data
public class NoticeReadUpdateRequest {
    private Long id;
    private Boolean readOrNot;

    @Builder
    public NoticeReadUpdateRequest(Long id, Boolean readOrNot) {
        this.id = id;
        this.readOrNot = readOrNot;
    }
}
