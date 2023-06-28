package org.zerock.moamoa.domain.DTO.party;

import org.zerock.moamoa.domain.entity.Product;

import lombok.Builder;
import lombok.Data;

@Data
public class PartyRequest {
	private Long id;
	private String address;
	private Integer count;
	private Long buyer;
	private Product product;

	@Builder
	public PartyRequest(Long id, String address, Integer count, Long buyer, Product product) {
		this.id = id;
		this.address = address;
		this.count = count;
		this.buyer = buyer;
		this.product = product;
	}
}
