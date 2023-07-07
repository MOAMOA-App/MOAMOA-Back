package org.zerock.moamoa.domain.DTO.party;

import java.time.Instant;

import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

import lombok.Data;

@Data
public class PartyResponse {
	private Long id;
	private String address;
	private Integer count;
	private Instant createdAt;
	private User buyer;
	private Product product;

	public PartyResponse(Party party) {
		id = party.getId();
		address = party.getAddress();
		count = party.getCount();
		createdAt = party.getCreatedAt();
		buyer = party.getBuyer();
		product = party.getProduct();
	}
}