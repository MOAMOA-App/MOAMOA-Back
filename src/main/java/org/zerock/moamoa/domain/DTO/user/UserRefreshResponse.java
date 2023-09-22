package org.zerock.moamoa.domain.DTO.user;

import lombok.Data;

@Data
public class UserRefreshResponse {
    String accessToken;
    String message;

    public static UserRefreshResponse toDto(String refreshToken, String message) {
        UserRefreshResponse response = new UserRefreshResponse();
        response.accessToken = refreshToken;
        response.message = message;
        return response;
    }

    public static UserRefreshResponse toDto(String message) {
        UserRefreshResponse response = new UserRefreshResponse();
        response.message = message;
        return response;
    }
}
