package org.zerock.moamoa.domain.DTO.product;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.enums.ProductStatus;

@Data
public class ProductStatusUpdateRequest {
    private Long productId;
    private ProductStatus status;

    @Builder
    public ProductStatusUpdateRequest(Long productId, ProductStatus status) {
        this.productId = productId;
        this.status = status;
    }
}
