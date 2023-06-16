package org.zerock.moamoa.domain.DTO;

import lombok.Data;
import org.zerock.moamoa.domain.entity.Announce;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.service.ProductService;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    private List<AnnounceDTO> announces;

    public ProductDTO(Product p){
        id = p.getId();
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

        user = p.getUser();

        announces = p.getAnnounces().stream()
                .map(announce -> new AnnounceDTO(announce))
                .collect(Collectors.toList());
    }
}
