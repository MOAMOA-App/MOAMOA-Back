package org.zerock.moamoa.domain.DTO.product;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.entity.ProductImages;

import java.util.List;

@Data
public class ProductTitleResponse {
    private Long id;
    private String title;
    private String mainImage;

    @Builder
    public ProductTitleResponse(Long id, String title,
                                List<ProductImages> productImages) {
        this.id = id;
        this.title = title;
        this.mainImage = !productImages.isEmpty() ? productImages.get(0).getFileName() : null;
    }
}
