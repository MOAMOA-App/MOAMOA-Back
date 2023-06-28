package org.zerock.moamoa.domain.DTO;

import java.time.Instant;

import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.ProductStatus;

import lombok.Data;

@Data
public class MyinfoDTO {
	private Long id;

	private User user;

	private Long categoryId;

	private String title;

	private ProductStatus status;

	private Instant createdAt;

	private int sellCount;

	private int maxCount;

	private String choiceSend;

	// 없는것: 메인 이미지(image), 참여 일자(partied_at)

	public MyinfoDTO(Product product) {
		id = product.getId();
		user = product.getUser();
		categoryId = product.getCategoryId();
		title = product.getTitle();
		status = product.getStatus();
		createdAt = product.getCreatedAt();
		sellCount = product.getSellCount();
		maxCount = product.getMaxCount();
		choiceSend = product.getChoiceSend();
	}
}
