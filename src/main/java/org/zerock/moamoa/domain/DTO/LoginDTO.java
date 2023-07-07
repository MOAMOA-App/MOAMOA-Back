package org.zerock.moamoa.domain.DTO;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginDTO {
	private String email;
	private String password;

	@Builder
	public LoginDTO(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
