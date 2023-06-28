package org.zerock.moamoa.domain.DTO.product;

import java.time.Instant;

import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.ProductStatus;

import lombok.Builder;
import lombok.Data;

@Data

public class ProductSaveRequest {
	private User user;
	private String title;
	private String description;
	private Long categoryId;
	private String sellingArea;
	private String detailArea;
	private Integer maxCount;
	private String choiceSend;
	private Integer countImage;
	private Instant finishedAt;
	private Integer sellPrice;
	private Integer viewCount = 0;
	private Integer sellCount = 0;
	private ProductStatus status = ProductStatus.READY;
	private Boolean activate = true;

	@Builder
	public ProductSaveRequest(User user, String title, String description, Long categoryId, String sellingArea,
		String detailArea, Integer maxCount, String choiceSend, Integer countImage,
		Instant finishedAt, ProductStatus productStatus) {
		this.user = user;
		this.title = title;
		this.description = description;
		this.categoryId = categoryId;
		this.sellingArea = sellingArea;
		this.detailArea = detailArea;
		this.maxCount = maxCount;
		this.choiceSend = choiceSend;
		this.countImage = countImage;
		this.finishedAt = finishedAt;
		this.status = productStatus;
	}
}
