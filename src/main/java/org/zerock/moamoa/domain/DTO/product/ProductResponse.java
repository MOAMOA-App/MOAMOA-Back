package org.zerock.moamoa.domain.DTO.product;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.zerock.moamoa.domain.DTO.productImage.ImageMapper;
import org.zerock.moamoa.domain.DTO.user.UserProfileResponse;
import org.zerock.moamoa.domain.entity.ProductImages;
import org.zerock.moamoa.domain.enums.Category;
import org.zerock.moamoa.domain.enums.ProductStatus;
import org.zerock.moamoa.utils.file.dto.FileResponse;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class ProductResponse {
    private Long id;
    private UserProfileResponse user;
    private Category categoryId;
    private String sellingArea;
    private String detailArea;
    private String title;
    private ProductStatus status;
    private Integer sellPrice;
    private Integer viewCount;
    private String description;
    private Integer sellCount;
    private Integer maxCount;
    private String choiceSend;
    private Instant createdAt;
    private Instant finishedAt;
    private Instant updatedAt;
    private List<FileResponse> productImages;

    @Builder
    public ProductResponse(Long id, UserProfileResponse user, Category categoryId, String sellingArea, String detailArea,
                           String title, String description, ProductStatus status, Integer sellPrice, Integer viewCount, Integer sellCount,
                           Integer maxCount, String choiceSend, Instant createdAt, Instant finishedAt, Instant updatedAt, List<ProductImages> productImages) {
        this.id = id;
        this.user = user;
        this.categoryId = categoryId;
        this.sellingArea = sellingArea;
        this.detailArea = detailArea;
        this.title = title;
        this.status = status;
        this.sellPrice = sellPrice;
        this.viewCount = viewCount;
        this.description = description;
        this.sellCount = sellCount;
        this.maxCount = maxCount;
        this.choiceSend = choiceSend;
        this.createdAt = createdAt;
        this.finishedAt = finishedAt;
        this.updatedAt = updatedAt;
        this.productImages = productImages != null ? productImages.stream().map(ImageMapper.INSTANCE::toDto).toList() : new ArrayList<>();
    }

}
