package org.zerock.moamoa.utils.file.dto;

import lombok.Data;

import java.util.List;

@Data
public class FileResultResponse {
    String message;
    List<String> urls;

    public static FileResultResponse toDto(String result, List<String> urls) {
        FileResultResponse response = new FileResultResponse();
        response.message = result;
        response.urls = urls;
        return response;
    }

    public static FileResultResponse toDto(String result) {
        FileResultResponse response = new FileResultResponse();
        response.message = result;
        return response;
    }
}
