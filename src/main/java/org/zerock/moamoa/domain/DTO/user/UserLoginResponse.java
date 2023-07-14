package org.zerock.moamoa.domain.DTO.user;

import org.zerock.moamoa.domain.entity.Auth;

import lombok.Builder;
import lombok.Data;

@Data
public class UserLoginResponse {
	private String tokenType;
	private String accessToken;
	private String refreshToken;

	@Builder
	public UserLoginResponse(Auth entity) {
		this.tokenType = entity.getTokenType();
		this.accessToken = entity.getAccessToken();
		this.refreshToken = entity.getRefreshToken();
	}
}
