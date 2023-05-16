package org.zerock.moamoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.domain.entity.File;
import org.zerock.moamoa.domain.entity.ProductImage;
import org.zerock.moamoa.repository.FileRepository;
import org.zerock.moamoa.repository.ProductImageRepository;

import java.io.FileNotFoundException;

@Service
public class ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final FileRepository fileRepository;

    @Autowired
    public ProductImageService(ProductImageRepository productImageRepository, FileRepository fileRepository) {
        this.productImageRepository = productImageRepository;
        this.fileRepository = fileRepository;
    }

    @Transactional
    public ProductImage saveProductImage(Long fileId) throws FileNotFoundException {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));

        ProductImage productImage = new ProductImage();
        productImage.setFile(file);

        return productImageRepository.save(productImage);
    }

    @Transactional
    public void removeProductImage(Long productImageId) throws FileNotFoundException {
        ProductImage productImage = productImageRepository.findById(productImageId)
                .orElseThrow(() -> new FileNotFoundException("ProductImage not found with id " + productImageId));

        File file = productImage.getFile();

        // 제거할 ProductImage와 연관된 File 제거
        productImage.setFile(null);
        file.setProductImage(null);
        fileRepository.save(file);

        // ProductImage 제거
        productImageRepository.delete(productImage);
    }

}
