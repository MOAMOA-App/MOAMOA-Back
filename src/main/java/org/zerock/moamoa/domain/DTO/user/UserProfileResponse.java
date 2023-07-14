package org.zerock.moamoa.domain.DTO.user;

import org.zerock.moamoa.domain.entity.User;

import lombok.Builder;
import lombok.Data;

@Data
public class UserProfileResponse {
	private Long id;
	private String nick;

	private String profImg;

	private String email;

	private String address;

	private String detailAddress;

	@Builder
	public UserProfileResponse(Long id, String nick, String profImg, String email, String address,
		String detailAddress) {
		this.id = id;
		this.nick = nick;
		this.profImg = profImg;
		this.email = email;
		this.address = address;
		this.detailAddress = detailAddress;
	}

	public UserProfileResponse(User user) {
		id = user.getId();
		nick = user.getNick();
		profImg = user.getProfImg();
		email = user.getEmail();
		address = user.getAddress();
		detailAddress = user.getDetailAddress();
	}
}
