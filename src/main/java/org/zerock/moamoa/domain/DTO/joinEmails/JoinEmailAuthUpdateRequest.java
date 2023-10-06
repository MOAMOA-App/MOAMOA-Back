package org.zerock.moamoa.domain.DTO.joinEmails;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
public class JoinEmailAuthUpdateRequest {
    // authenticate 상태 업데이트할떄 사용
    private String token;
    private String code;
    private Instant submissionTime;

    @Builder
    public JoinEmailAuthUpdateRequest(String token, String code) {
        this.token = token;
        this.code = code;
        this.submissionTime = Instant.now();
    }
}
