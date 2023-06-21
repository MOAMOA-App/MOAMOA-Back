package org.zerock.moamoa.domain.DTO;

import lombok.Data;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

import java.time.LocalDateTime;

@Data
public class MyinfoDTO {
    private Long id;

    private User user;

    private Long categoryId;

    private String title;

    private String status;

    private LocalDateTime createdAt;

    private int sellCount;

    private int maxCount;

    private String choiceSend;

    // 없는것: 메인 이미지(image), 참여 일자(partied_at)

    public MyinfoDTO(Product product) {
        id = product.getId();
        user = product.getUser();
        categoryId = product.getCategoryId();
        title = product.getTitle();
        status = product.getStatus();
        createdAt = product.getCreatedAt();
        sellCount = product.getSellCount();
        maxCount = product.getMaxCount();
        choiceSend = product.getChoiceSend();
    }
}
