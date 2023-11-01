package org.zerock.moamoa.domain.DTO.party;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.domain.DTO.user.UserProductResponse;
import org.zerock.moamoa.domain.DTO.user.UserProfileResponse;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

import lombok.Data;
import org.zerock.moamoa.utils.TimeUtils;

@Data
public class PartyResponse {
	private Long id;
	private String address;
	private Integer count;
	private LocalDateTime createdAt;
	private UserProfileResponse buyer;
	private ProductResponse product;

//	public PartyResponse(Party party) {
//		id = party.getId();
//		address = party.getAddress();
//		count = party.getCount();
//		createdAt = party.getCreatedAt();
//		buyer = UserProfileResponse.builder(party.getBuyer());
//		product = ProductResponse.builder();
//	}

	@Builder
	public PartyResponse(Long id, String address, Integer count, Instant createdAt,
						 UserProfileResponse buyer, ProductResponse product) {
		this.id = id;
		this.address = address;
		this.count = count;
		this.createdAt = TimeUtils.toLocalTime(createdAt);
		this.buyer = buyer;
		this.product = product;
	}
}