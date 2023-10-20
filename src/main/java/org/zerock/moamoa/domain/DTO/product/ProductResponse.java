package org.zerock.moamoa.domain.DTO.product;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.zerock.moamoa.domain.DTO.productImage.ImageMapper;
import org.zerock.moamoa.domain.DTO.user.UserProductResponse;
import org.zerock.moamoa.domain.entity.ProductImages;
import org.zerock.moamoa.domain.enums.ProductStatus;
import org.zerock.moamoa.utils.TimeUtils;
import org.zerock.moamoa.utils.file.dto.FileResponse;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class ProductResponse {
    private Long id;
    private UserProductResponse user;
    private String category;
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
    private double longtitue;
    private double lattitue;
    private String createdAt;
    private String finishedAt;
    private List<FileResponse> productImages;


    @Builder
    public ProductResponse(Long id, UserProductResponse user, String category, String sellingArea, String detailArea,
                           String title, String description, ProductStatus status, Integer sellPrice, Integer viewCount, Integer sellCount,
                           Integer maxCount, String choiceSend, Instant createdAt, Instant finishedAt, double longtitue, double lattitue,
                           List<ProductImages> productImages) {
        this.id = id;
        this.user = user;
        this.category = category;
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
        this.createdAt = TimeUtils.toLocalTime(createdAt);
        this.finishedAt = TimeUtils.toLocalTime(finishedAt);
        this.longtitue = longtitue;
        this.lattitue = lattitue;
        this.productImages = productImages != null ? productImages.stream().map(ImageMapper.INSTANCE::toDto).toList() : new ArrayList<>();
    }

}
