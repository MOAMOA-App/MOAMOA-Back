package org.zerock.moamoa.domain.DTO.announce;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class AnnounceResponse {
    private String message;
    private Long id;
    private Boolean lock;
    private String contents;
    private Instant createdAt;
    private Instant updatedAt;

    @Builder
    public AnnounceResponse(Long id, Boolean lock, String contents, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.lock = lock;
        this.contents = contents;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public static AnnounceResponse toMessage(String message) {
        AnnounceResponse response = new AnnounceResponse();
        response.message = message;
        return response;
    }
}
