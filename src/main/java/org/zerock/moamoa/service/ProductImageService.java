package org.zerock.moamoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.moamoa.domain.entity.File;
import org.zerock.moamoa.domain.entity.ProductImage;
import org.zerock.moamoa.repository.FileRepository;
import org.zerock.moamoa.repository.ProductImageRepository;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    public String saveFileLocally(MultipartFile file) throws IOException {
        String filePath = "C:/moamoa_temp";  // 로컬에 저장할 경로를 지정합니다.
                                            // 실제 사용 환경에 맞게 변경 (Ex: C:/uploads)
        // 파일을 저장하는 로직은 파일 저장 경로와 관련된 설정 등 추가 작업이 필요할 수 있습니다.
        // (디렉토리가 존재하지 않을 경우에는 디렉토리를 먼저 생성해야 할 수도)
        // 이 부분은 사용하는 프레임워크나 환경에 따라 상이할 수 있으므로, 필요한 설정을 추가하셔야 합니다.
        String fileName = file.getOriginalFilename();
        String savedFilePath = filePath + "/" + fileName;
        java.io.File savedFile = Paths.get(savedFilePath).toFile();

        // 파일을 로컬에 저장합니다.
        try (OutputStream outputStream = Files.newOutputStream(savedFile.toPath())) {
            outputStream.write(file.getBytes());
        }

        return savedFilePath;
    }

}
