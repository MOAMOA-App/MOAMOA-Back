package org.zerock.moamoa.service;

import javassist.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.domain.entity.File;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.FileRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
@Transactional
@SpringBootTest
class FileServiceTest {
    @Mock
    private FileRepository fileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FileService fileService;

//    @BeforeEach
//    public void setUp() {
//        User user = new User();
//        user.setId(1L);
//        user.setName("John Doe");
//        user.setEmail("john.doe@example.com");
//
//        file = new File();
//        file.setId(1L);
//        file.setType("PDF");
//        file.setCreatedAt(LocalDateTime.now());
//        file.setUsers(user);
//    }

    @AfterEach
    void tearDown() {
        fileRepository.deleteAll();
        userRepository.deleteAll();
    }

//    @Test
//    void saveFile() throws NotFoundException {
//        // 테스트용 User 생성
//        User user = new User();
//        user.setLoginType("Type");
//        user.setToken("Token");
//        user.setName("John Doe");
//        user.setEmail("john@example.com");
//        user.setNick("john");
//        user.setProfImg("profile.jpg");
//        user = userRepository.save(user);
//
//        // 사용자(User) 저장
//        User savedUser = userRepository.save(user);
//
//        // 테스트용 File 생성
//        File file = new File();
//        file.setType("image");
//        file.setUsers(savedUser);
//        file.setCreatedAt(LocalDateTime.now());
//
//        // 파일(File) 저장
//        File savedFile = fileService.saveFile(file, savedUser);
//
//        // userRepository.findById()를 통해 User를 가져올 필요 없이
//        // User 객체를 직접 생성하여 File에 설정
//
//        // UserRepository의 findById() 메서드가 가상의 User 객체를 반환하도록 설정
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        // FileRepository의 save() 메서드가 가상의 File 객체를 반환하도록 설정
//        when(fileRepository.save(any(File.class))).thenReturn(file);
//
//        // saveFile() 메서드 호출
//
//
//        // 결과 확인
//        assertNotNull(savedFile);
//        assertEquals(1L, savedFile.getId().longValue());
//        assertEquals("image/jpeg", savedFile.getType());
//        assertEquals(user, savedFile.getUsers());
//
//        // UserRepository의 findById() 메서드가 정확히 한 번 호출되었는지 확인
//        verify(userRepository, times(1)).findById(1L);
//
//        // FileRepository의 save() 메서드가 정확히 한 번 호출되었는지 확인
//        verify(fileRepository, times(1)).save(any(File.class));
//
//
////        when(fileRepository.save(any(File.class))).thenReturn(file);
////
////        File savedFile = fileService.saveFile(file);
////
////        verify(fileRepository).save(any(File.class));
////        Assertions.assertNotNull(savedFile);
////        Assertions.assertEquals(file.getId(), savedFile.getId());
////        Assertions.assertEquals(file.getType(), savedFile.getType());
////        Assertions.assertEquals(file.getCreatedAt(), savedFile.getCreatedAt());
////        Assertions.assertEquals(file.getUsers(), savedFile.getUsers());
//
////
//
////        // given
////        User user = new User();
////        user.setLoginType("test");
////        user.setToken("test");
////        user.setName("test");
////        user.setEmail("test@test.com");
////        user.setNick("test");
////        user.setProfImg("test");
////        userRepository.save(user);
////
////        // when
////        File savedFile = fileService.saveFile(user.getId(), "pdf", "example.pdf");
////
////        // then
////        assertNotNull(savedFile.getId());
////        assertEquals(user, savedFile.getUsers());
////        assertEquals("pdf", savedFile.getType());
////        assertNotNull(savedFile.getCreatedAt());
//    }

    @Test
    void findAll() {
        System.out.println(fileService.findAll());
    }
    @Test
    void findById() {
//        User user = new User();
//        user.setLoginType("test");
//        user.setToken("test");
//        user.setName("test");
//        user.setEmail("test@test.com");
//        user.setNick("test");
//        user.setProfImg("test");
//        userRepository.save(user);
//
//        String type = "image";
//        String name = "file1.jpg";
//
//        File file = fileService.saveFile(user.getId(), type, name);
//        Long fileId = file.getId();
//
//        System.out.println(fileService.findById(fileId));

//

//        // given
//        User user = new User();
//        user.setLoginType("test");
//        user.setToken("test");
//        user.setName("test");
//        user.setEmail("test@test.com");
//        user.setNick("test");
//        user.setProfImg("test");
//        userRepository.save(user);
//
//        String type = "test_type";
//        String name = "test_name";
//
//        File savedFile = fileService.saveFile(user.getId(), type, name);
//
//        // when
//        Optional<File> optionalFile = fileService.findById(savedFile.getId());
//
//        // then
//        assertTrue(optionalFile.isPresent());
//
//        File foundFile = optionalFile.get();
//        assertNotNull(foundFile.getId());
//        assertEquals(type, foundFile.getType());
//        assertNotNull(foundFile.getCreatedAt());
//        assertEquals(user.getId(), foundFile.getUsers().getId());
    }

    @Test
    void updateFile() {
//        // given
//        File file = fileService.saveFile(1L, "image", "file1.jpg");
//        String updatedType = "text";
//        String updatedName = "file1_updated.txt";
//
//        // when
//        File updatedFile = fileService.updateFile(file.getId(), updatedType, updatedName);
//
//        // then
//        assertNotNull(updatedFile);
//        assertEquals(updatedType, updatedFile.getType());
    }

    @Test
    void deleteFile() {
//        // given
//        File file = fileService.saveFile(1L, "image", "file1.jpg");
//        Long fileId = file.getId();
//
//        // when
//        fileService.removeFile(fileId);
//
//        // then
//        assertFalse(fileService.findById(fileId).isPresent());
    }
}