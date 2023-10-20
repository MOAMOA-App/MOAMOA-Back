package org.zerock.moamoa.domain.DTO.product;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.ProductStatus;

@Data
public class ProductSaveRequest {
    private User user;
    private String title;
    private String description;
    private String category;
    private String sellingArea;
    private String detailArea;
    private Integer maxCount;
    private String choiceSend;
    private String finishedAt;
    private Integer sellPrice;
    private Integer viewCount = 0;
    private Integer sellCount = 0;
    private ProductStatus status;
    private double longtitue;
    private double lattitue;
    private Boolean activate = true;

    @Builder
    public ProductSaveRequest(String title, String description, String category, String sellingArea,
                              String detailArea, Integer maxCount, String choiceSend, double longtitue, double lattitue,
                              String finishedAt, ProductStatus productStatus) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.sellingArea = sellingArea;
        this.detailArea = detailArea;
        this.maxCount = maxCount;
        this.choiceSend = choiceSend;
        this.longtitue = longtitue;
        this.lattitue = lattitue;
        this.finishedAt = finishedAt;
        this.status = productStatus != null ? productStatus : ProductStatus.READY;
    }
}
