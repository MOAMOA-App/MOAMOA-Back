package org.zerock.moamoa.utils.file.dto;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.entity.Product;

import java.time.Instant;

@Data
public class FileResponse {
    private String fileName;
    private long fileSize;
    private String contentType;
    private Instant uploadTimeStamp;
    private Product product;

    @Builder
    public FileResponse(String fileName, long fileSize, String contentType, Instant uploadTimeStamp) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.uploadTimeStamp = uploadTimeStamp;
    }
}

