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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
    @InjectMocks
    private FileService fileService;

    @Mock
    private FileRepository fileRepository;

    @AfterEach
    void tearDown() {
        fileRepository.deleteAll();
    }

    @Test
    void saveFile() throws NotFoundException {
        // 가상의 사용자 정보 생성
        User user = new User();
        user.setLoginType("Type");
        user.setToken("Token");
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setNick("john");
        user.setProfImg("profile.jpg");

        // createFile 메서드 호출
        File createdFile = new File();
        createdFile.setId(1L);
        createdFile.setUser(user);
        createdFile.setType("image/png");
        createdFile.setCreatedAt(LocalDateTime.now());
        createdFile.setName("example.png");

        // Mock FileRepository
        FileRepository fileRepository = Mockito.mock(FileRepository.class);
        Mockito.when(fileRepository.save(Mockito.any(File.class))).thenReturn(createdFile);

        // Create FileService with mocked repository
        FileService fileService = new FileService(fileRepository);

        // Perform saveFile
        File savedFile = fileService.saveFile(user, "image/png", "example.png");

        // 파일이 정상적으로 생성되었는지 확인
        assertNotNull(savedFile);
        assertEquals(user, savedFile.getUser());
        assertEquals("image/png", savedFile.getType());
        assertEquals("example.png", savedFile.getName());

        // fileRepository.save 메서드가 올바르게 호출되었는지 확인
        verify(fileRepository, times(1)).save(Mockito.any(File.class));
    }

    @Test
    void findAll() {
        System.out.println(fileService.findAll());
    }

    @Test
    void findById() {
        // 가상의 사용자 정보 생성
        User user = new User();
        user.setLoginType("Type");
        user.setToken("Token");
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setNick("john");
        user.setProfImg("profile.jpg");

        // 가상의 파일 정보 생성
        File file = new File();
        file.setId(1L);
        file.setUser(user);
        file.setType("image/png");
        file.setCreatedAt(LocalDateTime.now());
        file.setName("example.png");

        // Mock FileRepository
        FileRepository fileRepository = Mockito.mock(FileRepository.class);
        Mockito.when(fileRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(file));

        // Create FileService with mocked repository
        FileService fileService = new FileService(fileRepository);

        // Perform findById
        Optional<File> foundFile = fileService.findById(1L);

        // 파일이 정상적으로 조회되었는지 확인
        assertTrue(foundFile.isPresent());
        assertEquals(file.getId(), foundFile.get().getId());
        assertEquals(file.getUser(), foundFile.get().getUser());
        assertEquals(file.getType(), foundFile.get().getType());
        assertEquals(file.getName(), foundFile.get().getName());

        // fileRepository.findById 메서드가 올바르게 호출되었는지 확인
        verify(fileRepository, times(1)).findById(Mockito.anyLong());
    }

    @Test
    void removeFile() {
        // 가상의 사용자 정보 생성
        User user = new User();
        user.setLoginType("Type");
        user.setToken("Token");
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setNick("john");
        user.setProfImg("profile.jpg");

        // 가상의 파일 정보 생성
        File file = new File();
        file.setId(1L);
        file.setUser(user);
        file.setType("image/png");
        file.setCreatedAt(LocalDateTime.now());
        file.setName("example.png");

        // Mock FileRepository
        FileRepository fileRepository = Mockito.mock(FileRepository.class);
        Mockito.when(fileRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(file));

        // Create FileService with mocked repository
        FileService fileService = new FileService(fileRepository);

        // Perform removeFile
        fileService.removeFile(1L);

        // fileRepository.findById와 fileRepository.deleteById 메서드가 올바르게 호출되었는지 확인
        verify(fileRepository, times(1)).findById(Mockito.anyLong());
        verify(fileRepository, times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void updateFile() {
        // 가상의 사용자 정보 생성
        User user = new User();
        user.setLoginType("Type");
        user.setToken("Token");
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setNick("john");
        user.setProfImg("profile.jpg");

        // 가상의 파일 정보 생성
        File file = new File();
        file.setId(1L);
        file.setUser(user);
        file.setType("image/png");
        file.setCreatedAt(LocalDateTime.now());
        file.setName("example.png");

        // Mock FileRepository
        FileRepository fileRepository = Mockito.mock(FileRepository.class);
        Mockito.when(fileRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(file));
        Mockito.when(fileRepository.save(Mockito.any(File.class))).thenReturn(file);

        // Create FileService with mocked repository
        FileService fileService = new FileService(fileRepository);

        // Perform updateFile
        File updatedFile = fileService.updateFile(1L, "image/jpeg", "new_example.jpg");

        // 파일이 정상적으로 업데이트되었는지 확인
        assertEquals("image/jpeg", updatedFile.getType());
        assertEquals("new_example.jpg", updatedFile.getName());

        // fileRepository.findById와 fileRepository.save 메서드가 올바르게 호출되었는지 확인
        verify(fileRepository, times(1)).findById(Mockito.anyLong());
        verify(fileRepository, times(1)).save(Mockito.any(File.class));
    }
}