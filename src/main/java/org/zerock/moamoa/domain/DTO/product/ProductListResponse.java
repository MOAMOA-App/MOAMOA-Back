package org.zerock.moamoa.domain.DTO.product;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.DTO.user.UserProductResponse;
import org.zerock.moamoa.domain.entity.ProductImages;
import org.zerock.moamoa.domain.enums.ProductStatus;
import org.zerock.moamoa.utils.TimeUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductListResponse {
    private Long id;
    private UserProductResponse user;
    private String category;
    private String sellingArea;
    private String title;
    private ProductStatus status;
    private Integer sellPrice;
    private Integer viewCount;
    private Integer sellCount;
    private Integer maxCount;
    private String choiceSend;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private String mainImage;


    @Builder
    public ProductListResponse(Long id, UserProductResponse user, String category, String sellingArea,
                               String title, ProductStatus status, Integer sellPrice, Integer viewCount, Integer sellCount,
                               Integer maxCount, String choiceSend, Instant createdAt, Instant finishedAt,
                               List<ProductImages> productImages) {
        this.id = id;
        this.user = user;
        this.category = category;
        this.sellingArea = sellingArea;
        this.title = title;
        this.status = status;
        this.sellPrice = sellPrice;
        this.viewCount = viewCount;
        this.sellCount = sellCount;
        this.maxCount = maxCount;
        this.choiceSend = choiceSend;
        this.createdAt = TimeUtils.toLocalTime(createdAt);
        this.finishedAt = TimeUtils.toLocalTime(finishedAt);
        this.mainImage = !productImages.isEmpty() ? productImages.get(0).getFileName() : null;
    }
}
