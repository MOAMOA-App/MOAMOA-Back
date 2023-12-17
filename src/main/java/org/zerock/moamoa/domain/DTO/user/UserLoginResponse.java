package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.entity.Auth;
import org.zerock.moamoa.domain.entity.User;

@Data
public class UserLoginResponse {
    private String message;
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private UserResponse user;

    @Builder
    public UserLoginResponse(String message, Auth entity, User user) {
        this.message = message;
        this.tokenType = entity.getTokenType();
        this.accessToken = entity.getAccessToken();
        this.refreshToken = entity.getRefreshToken();
        this.user = UserResponse.builder(user);
    }

    @Builder
    public UserLoginResponse(String message) {
        this.message = message;
    }
}
