package org.zerock.moamoa.domain.DTO.announce;

import lombok.Data;

@Data
public class AnnounceResultResponse {
    AnnounceResponse response;
    String message;

    public static AnnounceResultResponse toDto(String message, AnnounceResponse announceResponse) {
        AnnounceResultResponse response = new AnnounceResultResponse();
        response.response = announceResponse;
        response.message = message;
        return response;
    }

    public static AnnounceResultResponse toMessage(String message) {
        AnnounceResultResponse response = new AnnounceResultResponse();
        response.message = message;
        return response;
    }
}
