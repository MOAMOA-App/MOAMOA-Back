package org.zerock.moamoa.domain.DTO.product;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.enums.ProductStatus;
import org.zerock.moamoa.utils.TimeUtils;

import java.time.Instant;

@Data
public class ProductUpdateRequest {
    private Long productId;
    private String title;
    private String description;
    private String category;
    private String sellingArea;
    private String detailArea;
    private Integer sellPrice;
    private Integer viewCount;
    private Integer maxCount;
    private String choiceSend;
    private Integer countImage;
    private Instant finishedAt;
    private ProductStatus status;

    @Builder
    public ProductUpdateRequest(Long productId, String title, String description, String category, String sellingArea,
                                String detailArea, Integer sellPrice, Integer viewCount, Integer maxCount, String choiceSend,
                                Integer countImage, String finishedAt, ProductStatus status) {
        this.productId = productId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.sellingArea = sellingArea;
        this.detailArea = detailArea;
        this.sellPrice = sellPrice;
        this.viewCount = viewCount;
        this.maxCount = maxCount;
        this.choiceSend = choiceSend;
        this.countImage = countImage;
        this.finishedAt = TimeUtils.toInstant(finishedAt);
        this.status = status;
    }
}
