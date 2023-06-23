package org.zerock.moamoa.domain.DTO;

import lombok.Data;
import org.zerock.moamoa.domain.entity.Announce;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.service.ProductService;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductDTO {
    private Long id;

    private UserDTO user;

    private Long categoryId;

    private String sellingArea;

    private String detailArea;

    private String title;

    private String status;

    private Integer sellPrice;

    private Integer viewCount;

    private String description;

    private Instant createdAt;

    private Instant finishedAt;

    private Instant updatedAt;

    private Integer sellCount;

    private Integer maxCount;

    private String choiceSend;

    private Integer countImage;

    private List<AnnounceDTO> announces;

    public ProductDTO fromEntity(Product p){
        ProductDTO dto = new ProductDTO();
        dto.id = p.getId();
        dto.categoryId = p.getCategoryId();
        dto.sellingArea = p.getSellingArea();
        dto.detailArea = p.getDetailArea();
        dto.title = p.getTitle();
        dto.status = p.getStatus();
        dto.viewCount = p.getViewCount();
        dto.description = p.getDescription();
        dto.finishedAt = p.getFinishedAt();
        dto.sellCount = p.getSellCount();
        dto.maxCount = p.getMaxCount();
        dto.choiceSend = p.getChoiceSend();
        dto.countImage = p.getCountImage();

        dto.user = new UserDTO(p.getUser());

        dto.announces = p.getAnnounces().stream()
                .map(AnnounceDTO::new)
                .collect(Collectors.toList());

        return dto;
    }
}
