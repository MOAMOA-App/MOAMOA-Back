package org.zerock.moamoa.domain.DTO.announce;

import org.zerock.moamoa.domain.entity.User;

import lombok.Builder;
import lombok.Data;

@Data
public class AnnounceRequest {
	private Long id;
	private Boolean lock;
	private String contents;
	private User user;

	@Builder
	public AnnounceRequest(Long id, Boolean lock, String contents, User user) {
		this.id = id;
		this.lock = lock;
		this.contents = contents;
		this.user = user;
	}

}
