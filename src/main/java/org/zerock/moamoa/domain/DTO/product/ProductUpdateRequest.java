package org.zerock.moamoa.domain.DTO.product;

import java.time.Instant;

import org.zerock.moamoa.domain.enums.Category;
import org.zerock.moamoa.domain.enums.ProductStatus;

import lombok.Builder;
import lombok.Data;

@Data
public class ProductUpdateRequest {

	private Long id;
	private String title;
	private String description;
	private Category categoryId;
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
	public ProductUpdateRequest(Long id, String title, String description, Category categoryId, String sellingArea,
		String detailArea, Integer sellPrice, Integer viewCount, Integer maxCount, String choiceSend,
		Integer countImage,
		Instant finishedAt, ProductStatus status) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.categoryId = categoryId;
		this.sellingArea = sellingArea;
		this.detailArea = detailArea;
		this.sellPrice = sellPrice;
		this.viewCount = viewCount;
		this.maxCount = maxCount;
		this.choiceSend = choiceSend;
		this.countImage = countImage;
		this.finishedAt = finishedAt;
		this.status = status;
	}
}
