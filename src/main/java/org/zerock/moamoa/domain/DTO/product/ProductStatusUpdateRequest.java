package org.zerock.moamoa.domain.DTO.product;

import org.zerock.moamoa.domain.enums.ProductStatus;

import lombok.Builder;
import lombok.Data;

@Data
public class ProductStatusUpdateRequest {
	private Long id;
	private ProductStatus status;

	@Builder
	public ProductStatusUpdateRequest(Long id, ProductStatus status) {
		this.id = id;
		this.status = status;
	}
}
