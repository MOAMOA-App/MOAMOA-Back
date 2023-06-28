package org.zerock.moamoa.domain.DTO;

import org.zerock.moamoa.domain.entity.User;

import lombok.Builder;
import lombok.Data;

@Data
public class UserDTO {
	private Long id;

	private String name;

	private String password;

	private String nick;

	private String profImg;

	private String email;

	private String address;

	private String detailAddress;

	@Builder
	public UserDTO(Long id, String name, String password, String nick, String profImg, String email, String address,
		String detailAddress) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.nick = nick;
		this.profImg = profImg;
		this.email = email;
		this.address = address;
		this.detailAddress = detailAddress;
	}

	public UserDTO(User user) {
		id = user.getId();
		name = user.getName();
		password = user.getPassword();
		nick = user.getNick();
		profImg = user.getProfImg();
		email = user.getEmail();
		address = user.getAddress();
		detailAddress = user.getDetailAddress();
	}
}
