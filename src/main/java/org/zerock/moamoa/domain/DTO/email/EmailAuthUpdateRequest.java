package org.zerock.moamoa.domain.DTO.email;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
public class EmailAuthUpdateRequest {
    // authenticate 상태 업데이트할떄 사용
    private String token;
    private String code;
    private Instant submissionTime;

    @Builder
    public EmailAuthUpdateRequest(String token, String code) {
        this.token = token;
        this.code = code;
        this.submissionTime = Instant.now();
    }
}
