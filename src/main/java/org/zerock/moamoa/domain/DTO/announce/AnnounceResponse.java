package org.zerock.moamoa.domain.DTO.announce;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
public class AnnounceResponse {
	private Long id;
	private Boolean lock;
	private String contents;
	private Instant createdAt;
	private Instant updatedAt;

	@Builder
	public AnnounceResponse(Long id, Boolean lock, String contents, Instant createdAt, Instant updatedAt) {
		this.id = id;
		this.lock = lock;
		this.contents = contents;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
