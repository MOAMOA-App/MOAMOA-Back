package org.zerock.moamoa.domain.DTO.product;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.enums.ProductStatus;

@Data
public class ProductStatusUpdateRequest {
    private ProductStatus status;

    @Builder
    public ProductStatusUpdateRequest(ProductStatus status) {
        this.status = status;
    }
}
