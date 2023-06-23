package org.zerock.moamoa.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductImageServiceTest {
    @Mock
    private FileRepository fileRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @InjectMocks
    private ProductImageService productImageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        fileRepository.deleteAll();
        productImageRepository.deleteAll();
    }

    @Test
    void saveProductImage() throws FileNotFoundException {
        // 준비
        Long fileId = 1L;
        File file = new File();
        file.setId(fileId); // fileId와 연관된 File 객체 생성
        MockMultipartFile multipartFile = new MockMultipartFile("file", "filename.jpg", "image/jpeg", "test data".getBytes());

        // fileRepository.findById(fileId) 메소드 호출 시 위에서 생성한 File 객체 반환
        when(fileRepository.findById(fileId)).thenReturn(Optional.of(file));
        // productImageRepository.save(any(ProductImage.class)) 메소드 호출 시 전달된 ProductImage 객체 그대로 반환
        when(productImageRepository.save(any(ProductImage.class))).thenAnswer(invocation -> invocation.getArgument(0));


        // 실행
        // productImageService.saveProductImage(fileId) 메소드 호출해 실제 테스트 수행
        ProductImage savedProductImage = productImageService.saveProductImage(fileId);


        // 검증
        // fileRepository.findById(fileId) 메소드가 1번 호출되었는지 검증
        verify(fileRepository, times(1)).findById(fileId);
        // productImageRepository.save(any(ProductImage.class)) 메소드가 1번 호출되었는지 검증
        verify(productImageRepository, times(1)).save(any(ProductImage.class));

        // 반환된 ProductImage 객체의 getFile()을 통해 file 객체가 예상한 것과 동일한지 확인
        assertEquals(file, savedProductImage.getFile());
    }

    @Test
    void removeProductImage() throws FileNotFoundException {
        // 준비
        String productImageId = "1";
        ProductImage productImage = new ProductImage();
        productImage.setId(productImageId);
        File file = new File();
        file.setProductImage(productImage);
        productImage.setFile(file);

        when(productImageRepository.findById(Long.valueOf(productImageId))).thenReturn(Optional.of(productImage));

        // 실행
        productImageService.removeProductImage(Long.valueOf(productImageId));

        // 검증
        verify(productImageRepository, times(1)).findById(Long.valueOf(productImageId));
        verify(fileRepository, times(1)).save(file);
        verify(productImageRepository, times(1)).delete(productImage);
        assertNull(productImage.getFile());
        assertNull(file.getProductImage());
    }

    @Test
    void saveFileLocally() throws IOException {
        // 준비
        String filePath = "C:/moamoa_temp"; // 저장할 경로 설정
        String fileName = "test.jpg";
        String savedFilePath = filePath + "/" + fileName;

        // MultipartFile 모의 객체 생성
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                fileName,
                "image/jpeg",
                "test image".getBytes()
        );

        // 파일 저장 경로와 관련된 설정이 필요한 경우, fileRepository.save() 메소드 호출 전에 해당 설정을 추가하십시오.

        // fileRepository.save() 메소드가 호출되었을 때 반환할 값을 설정합니다.
        when(fileRepository.save(any(File.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        // 실행
        String resultFilePath = productImageService.saveFileLocally(multipartFile);

        // 검증
        assertEquals(savedFilePath, resultFilePath);
    }
}