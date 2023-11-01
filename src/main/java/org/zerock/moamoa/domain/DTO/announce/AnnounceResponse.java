package org.zerock.moamoa.domain.DTO.announce;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.utils.TimeUtils;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AnnounceResponse {
    private Long id;
    private Boolean lock;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public AnnounceResponse(Long id, Boolean lock, String contents, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.lock = lock;
        this.contents = contents;
        this.createdAt = TimeUtils.toLocalTime(createdAt);
        this.updatedAt = TimeUtils.toLocalTime(updatedAt);
    }

}
