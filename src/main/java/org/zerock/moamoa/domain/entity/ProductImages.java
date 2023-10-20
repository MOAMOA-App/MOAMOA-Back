package org.zerock.moamoa.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.utils.file.dto.FileResponse;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
public class ProductImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String fileName;
    private Long fileSize;
    private String contentType;
    private Instant uploadTimeStamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private Product product;


    @Builder
    public ProductImages(String fileName, Long fileSize, String contentType, Instant uploadTimeStamp, Product product) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.uploadTimeStamp = uploadTimeStamp;
        this.product = product;
    }

    public static ProductImages toEntity(Product product, FileResponse image) {
        ProductImages images = new ProductImages();
        images.fileName = image.getFileName();
        images.fileSize = image.getFileSize();
        images.contentType = image.getContentType();
        images.uploadTimeStamp = image.getUploadTimeStamp();
        images.product = product;
        return images;
    }
}
