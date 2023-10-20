package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.ProductImages;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.ProductImageRepository;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;
import org.zerock.moamoa.utils.file.ImageUtils;
import org.zerock.moamoa.utils.file.dto.FileRequest;
import org.zerock.moamoa.utils.file.dto.FileResponse;
import org.zerock.moamoa.utils.file.dto.FileResultResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FileService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;


    public FileResultResponse saveFile(FileRequest request, MultipartFile[] images, String username) {
        FileResultResponse response;
        String category = request.getCategory();
        switch (category) {
            case "profile" -> response = saveProfileImage(request, images, username);
            case "product" -> response = saveProduct(request, images, username);
            default -> response = FileResultResponse.toDto("CATEGORY_NOT_EXIST");
        }
        return response;
    }


    //미사용
    private FileResultResponse saveProduct(FileRequest request, MultipartFile[] images, String username) {
        String category = request.getCategory();
        Long idx = request.getIdx();
        if (productRepository.existsById(request.getIdx())) {
            Product product = productRepository.findByIdOrThrow(idx);
            User user = userRepository.findByEmailOrThrow(username);
            List<String> result = new ArrayList<>();
            if (product.getUser().equals(user)) {
                //이전 이미지 삭제
                List<ProductImages> befImages = product.getProductImages();
                productImageRepository.deleteAll(befImages);
                removeFiles(befImages.stream().map(ProductImages::getFileName).toList());

                product.setProductImages(Collections.emptyList());
                if (images[0].getSize() == 0) {
                    return FileResultResponse.toDto("OK", result);
                }
                List<FileResponse> saveImages = ImageUtils.saveFiles(images, category);
                List<ProductImages> newImages = saveImages.stream().map(image -> ProductImages.toEntity(product, image)).toList();
                productImageRepository.saveAll(newImages);
                result = saveImages.stream().map(FileResponse::getFileName).toList();
                return FileResultResponse.toDto("OK", result);
            }
            return FileResultResponse.toDto("AUTH_FAIL");
        }
        return FileResultResponse.toDto("ENTITY_NOT_EXIST");
    }

    /**
     * 프로필 사진 변경
     */
    public FileResultResponse saveProfileImage(FileRequest request, MultipartFile[] images, String username) {
        String category = request.getCategory();
        Long idx = request.getIdx();
        if (userRepository.existsByEmail(username)) {
            User user = userRepository.findByEmailOrThrow(username);
            List<String> result = new ArrayList<>();

            if (user.getProfImg() != null) removeFiles(user.getProfImg());
            if (images[0].getSize() == 0) {
                user.updateImage(null);
                return FileResultResponse.toDto("OK", result);
            }
            String cover = ImageUtils.saveFile(images[0], category).getFileName();
            user.updateImage(cover);
            result.add(user.getProfImg());
            return FileResultResponse.toDto("OK", result);
        }
        return FileResultResponse.toDto("ENTITY_NOT_EXIST");
    }

    /**
     * 파일 삭제
     */
    public void removeFiles(List<String> urls) {
        for (String url : urls) {
            ImageUtils.removeFile(url);
        }
    }

    public void removeFiles(String url) {
        ImageUtils.removeFile(url);
    }
}

