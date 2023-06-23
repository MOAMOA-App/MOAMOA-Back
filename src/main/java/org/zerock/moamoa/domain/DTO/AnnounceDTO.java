package org.zerock.moamoa.domain.DTO;

import lombok.Data;
import org.zerock.moamoa.domain.entity.Announce;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class AnnounceDTO {
    private Long id;
    private Boolean lock;
    private String contents;
    private Instant createdAt;

    public AnnounceDTO(Announce announce){
        id = announce.getId();
        lock = announce.getLock();
        contents = announce.getContents();
        createdAt = announce.getCreatedAt();
    }
}
