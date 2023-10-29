package org.zerock.moamoa.domain.DTO.email;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.enums.EmailType;

import java.time.Instant;

@Data
public class EmailAuthUpdateRequest {
    // authenticate 상태 업데이트할떄 사용
    private EmailType type;
    private String token;
    private String code;
    private Instant submissionTime;

    @Builder
    public EmailAuthUpdateRequest(EmailType type, String token, String code) {
        this.type = type;
        this.token = token;
        this.code = code;
        this.submissionTime = Instant.now();
    }
}
