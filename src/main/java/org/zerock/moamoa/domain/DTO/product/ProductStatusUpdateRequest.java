package org.zerock.moamoa.domain.DTO.product;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.enums.ProductStatus;

@Data
public class ProductStatusUpdateRequest {
    private Long product_id;
    private ProductStatus status;

    @Builder
    public ProductStatusUpdateRequest(Long product_id, ProductStatus status) {
        this.product_id = product_id;
        this.status = status;
    }
}
