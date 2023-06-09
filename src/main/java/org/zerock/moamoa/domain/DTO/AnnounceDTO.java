package org.zerock.moamoa.domain.DTO;

import lombok.Data;
import org.zerock.moamoa.domain.entity.Announce;

import java.time.LocalDateTime;

@Data
public class AnnounceDTO {
    private Long id;
    private int lock;
    private String contents;
    private LocalDateTime createdAt;

    public AnnounceDTO(Announce announce){
        id = announce.getId();
        lock = announce.getLock();
        contents = announce.getContents();
        createdAt = announce.getCreatedAt();
    }
}
