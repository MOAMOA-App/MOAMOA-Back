package org.zerock.moamoa.utils.file.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
public class FileResponse {
    private String fileName;
    private long fileSize;
    private String contentType;
    private Instant uploadTimeStamp;
    private String fileRoot;

    @Builder
    public FileResponse(String fileName, long fileSize, String contentType, Instant uploadTimeStamp, String fileRoot) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.uploadTimeStamp = uploadTimeStamp;
        this.fileRoot = fileRoot;
    }
}

