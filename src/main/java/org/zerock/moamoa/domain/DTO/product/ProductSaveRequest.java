package org.zerock.moamoa.domain.DTO.product;

import java.time.Instant;

import org.zerock.moamoa.domain.enums.Category;
import org.zerock.moamoa.domain.enums.ProductStatus;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.Builder;
import lombok.Data;

@Data
public class ProductSaveRequest {
	private Long userId;
	private String title;
	private String description;
	private Category categoryId;
	private String sellingArea;
	private String detailArea;
	private Integer maxCount;
	private String choiceSend;
	private Integer countImage;

	@FutureOrPresent    //오늘 이후 날짜만 허용
	private Instant finishedAt;
	private Integer sellPrice;
	private Integer viewCount = 0;
	private Integer sellCount = 0;
	private ProductStatus status;
	private Boolean activate = true;

	@Builder
	public ProductSaveRequest(String title, String description, Category categoryId, String sellingArea,
		String detailArea, Integer maxCount, String choiceSend, Integer countImage,
		Instant finishedAt, ProductStatus productStatus) {
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
