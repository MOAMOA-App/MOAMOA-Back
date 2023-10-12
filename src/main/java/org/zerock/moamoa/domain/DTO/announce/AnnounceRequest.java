package org.zerock.moamoa.domain.DTO.announce;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

@Data
public class AnnounceRequest {
    private Long id;
    private Boolean lock;
    private String contents;
    private User user;
    private Product product;

    @Builder
    public AnnounceRequest(Long id, Boolean lock, String contents, User user) {
        this.id = id;
        this.lock = lock;
        this.contents = contents;
        this.user = user;
    }

}
