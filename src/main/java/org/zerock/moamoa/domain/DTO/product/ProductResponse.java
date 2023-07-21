package org.zerock.moamoa.domain.DTO.product;

import java.time.Instant;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.DTO.announce.AnnounceResponse;
import org.zerock.moamoa.domain.DTO.user.UserProfileResponse;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.enums.Category;
import org.zerock.moamoa.domain.enums.ProductStatus;

import java.time.Instant;
import java.util.List;

@Data
public class ProductResponse {
	private Long id;
	private UserProfileResponse user;
	private Category categoryId;
	private String sellingArea;
	private String detailArea;
	private String title;
	private ProductStatus status;
	private Integer sellPrice;
	private Integer viewCount;
	private String description;
	private Integer sellCount;
	private Integer maxCount;
	private String choiceSend;
	private Integer countImage;
	private Instant createdAt;
	private Instant finishedAt;
	private Instant updatedAt;
	private List<AnnounceResponse> announces;
	private List<Party> parties;

	@Builder
	public ProductResponse(Long id, UserProfileResponse user, Category categoryId, String sellingArea,
		String detailArea,
		String title,
		ProductStatus status, Integer sellPrice, Integer viewCount, String description, Integer sellCount,
		Integer maxCount,
		String choiceSend, Integer countImage, Instant createdAt, Instant finishedAt, Instant updatedAt,
		List<AnnounceResponse> announces, List<Party> parties) {
		this.id = id;
		this.user = user;
		this.categoryId = categoryId;
		this.sellingArea = sellingArea;
		this.detailArea = detailArea;
		this.title = title;
		this.status = status;
		this.sellPrice = sellPrice;
		this.viewCount = viewCount;
		this.description = description;
		this.sellCount = sellCount;
		this.maxCount = maxCount;
		this.choiceSend = choiceSend;
		this.countImage = countImage;
		this.createdAt = createdAt;
		this.finishedAt = finishedAt;
		this.updatedAt = updatedAt;
		this.announces = announces;
		this.parties = parties;
	}

}
