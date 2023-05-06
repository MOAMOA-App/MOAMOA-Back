package org.zerock.moamoa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.moamoa.domain.entity.File;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.FileRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileServiceTest {
    @Autowired
    FileService fileService;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    void saveFile() {
        // given
        User user = new User();
        user.setLoginType("test");
        user.setToken("test");
        user.setName("test");
        user.setEmail("test@test.com");
        user.setNick("test");
        user.setProfImg("test");
        userRepository.save(user);

        String type = "test_type";
        String name = "test_name";

        // when
        File savedFile = fileService.saveFile(user.getId(), type, name);

        // then
        assertNotNull(savedFile.getId());
        assertEquals(type, savedFile.getType());
        assertNotNull(savedFile.getCreatedAt());
        assertEquals(user.getId(), savedFile.getUsers().getId());
    }

    @Test
    void findAll() {
        // given
        User user = new User();
        user.setLoginType("test");
        user.setToken("test");
        user.setName("test");
        user.setEmail("test@test.com");
        user.setNick("test");
        user.setProfImg("test");
        userRepository.save(user);

        List<File> files = new ArrayList<>();

        // 파일 10개 생성
        for (int i = 0; i < 10; i++) {
            String type = "test_type" + i;
            String name = "test_name" + i;
            files.add(fileService.saveFile(user.getId(), type, name));
        }

        // when
        List<File> foundFiles = fileService.findAll();

        // then
        assertEquals(10, foundFiles.size());

        // 파일이 올바르게 저장되었는지 확인
        for (int i = 0; i < 10; i++) {
            assertEquals(files.get(i).getId(), foundFiles.get(i).getId());
            assertEquals(files.get(i).getType(), foundFiles.get(i).getType());
            assertNotNull(foundFiles.get(i).getCreatedAt());
            assertEquals(user.getId(), foundFiles.get(i).getUsers().getId());
        }
    }

    @Test
    void findById() {
//        File file = fileService.saveFile(1L, "image", "file1.jpg");
//        Long fileId = file.getId();
//
//        System.out.println(fileService.findById(fileId));

        // given
        User user = new User();
        user.setLoginType("test");
        user.setToken("test");
        user.setName("test");
        user.setEmail("test@test.com");
        user.setNick("test");
        user.setProfImg("test");
        userRepository.save(user);

        String type = "test_type";
        String name = "test_name";

        File savedFile = fileService.saveFile(user.getId(), type, name);

        // when
        Optional<File> optionalFile = fileService.findById(savedFile.getId());

        // then
        assertTrue(optionalFile.isPresent());

        File foundFile = optionalFile.get();
        assertNotNull(foundFile.getId());
        assertEquals(type, foundFile.getType());
        assertNotNull(foundFile.getCreatedAt());
        assertEquals(user.getId(), foundFile.getUsers().getId());
    }

    @Test
    void updateFile() {
        // given
        File file = fileService.saveFile(1L, "image", "file1.jpg");
        String updatedType = "text";
        String updatedName = "file1_updated.txt";

        // when
        File updatedFile = fileService.updateFile(file.getId(), updatedType, updatedName);

        // then
        assertNotNull(updatedFile);
        assertEquals(updatedType, updatedFile.getType());
    }

    @Test
    void deleteFile() {
        // given
        File file = fileService.saveFile(1L, "image", "file1.jpg");
        Long fileId = file.getId();

        // when
        fileService.removeFile(fileId);

        // then
        assertFalse(fileService.findById(fileId).isPresent());
    }
}