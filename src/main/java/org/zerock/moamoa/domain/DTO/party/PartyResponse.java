package org.zerock.moamoa.domain.DTO.party;

import java.time.Instant;
import java.time.LocalDateTime;

import lombok.Builder;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.domain.DTO.user.UserResponse;

import lombok.Data;
import org.zerock.moamoa.utils.TimeUtils;

@Data
public class PartyResponse {
	private Long id;
	private String address;
	private Integer count;
	private LocalDateTime createdAt;
	private UserResponse buyer;
	private ProductResponse product;
	private Boolean status;

	@Builder
	public PartyResponse(Long id, String address, Integer count, Instant createdAt,
                         UserResponse buyer, ProductResponse product, Boolean status) {
		this.id = id;
		this.address = address;
		this.count = count;
		this.createdAt = TimeUtils.toLocalTime(createdAt);
		this.buyer = buyer;
		this.product = product;
		this.status = status;
	}
}