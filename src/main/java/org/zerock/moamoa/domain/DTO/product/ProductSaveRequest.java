package org.zerock.moamoa.domain.DTO.product;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.Category;
import org.zerock.moamoa.domain.enums.ProductStatus;

@Data
public class ProductSaveRequest {
    private User user;
    private String title;
    private String description;
    private Category categoryId;
    private String sellingArea;
    private String detailArea;
    private Integer maxCount;
    private String choiceSend;
    private Integer countImage;
    private String finishedAt;
    private Integer sellPrice;
    private Integer viewCount = 0;
    private Integer sellCount = 0;
    private ProductStatus status;
    private Boolean activate = true;

    @Builder
    public ProductSaveRequest(String title, String description, Category categoryId, String sellingArea,
                              String detailArea, Integer maxCount, String choiceSend, Integer countImage,
                              String finishedAt, ProductStatus productStatus) {
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.sellingArea = sellingArea;
        this.detailArea = detailArea;
        this.maxCount = maxCount;
        this.choiceSend = choiceSend;
        this.countImage = countImage;
        this.finishedAt = finishedAt;
        this.status = productStatus != null ? productStatus : ProductStatus.READY;
    }
}
