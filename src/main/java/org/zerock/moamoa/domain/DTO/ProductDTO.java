package org.zerock.moamoa.domain.DTO;

import lombok.Data;
import org.zerock.moamoa.domain.entity.Announce;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class ProductDTO {
    private Long id;

    private User user;

    private Long categoryId;

    private String sellingArea;

    private String detailArea;

    private String title;

    private String status;

    private int sellPrice;

    private int viewCount;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;

    private LocalDateTime updatedAt;

    private int sellCount;

    private int maxCount;

    private String choiceSend;

    public ProductDTO(Product p){
        id = p.getId();
        user = p.getUser();
        categoryId = p.getCategoryId();
        sellingArea = p.getSellingArea();
        detailArea = p.getDetailArea();
        title = p.getTitle();
        status = p.getStatus();
        viewCount = p.getViewCount();
        description = p.getDescription();
        createdAt = p.getCreatedAt();
        finishedAt = p.getFinishedAt();
        updatedAt = p.getUpdatedAt();
        sellCount = p.getSellCount();
        maxCount = p.getMaxCount();
        choiceSend = p.getChoiceSend();
    }
}
