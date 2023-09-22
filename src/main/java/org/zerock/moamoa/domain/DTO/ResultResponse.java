package org.zerock.moamoa.domain.DTO;

import lombok.Data;

@Data
public class ResultResponse {
    String message;

    public static ResultResponse toDto(String message) {
        ResultResponse response = new ResultResponse();
        response.message = message;
        return response;
    }
}
