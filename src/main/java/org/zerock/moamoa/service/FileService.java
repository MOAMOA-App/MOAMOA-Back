package org.zerock.moamoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.domain.entity.File;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.FileRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Autowired
    public FileService(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public File saveFile(Long userId, String type, String name) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + userId));

        File file = new File();
        file.setUsers(user);
        file.setType(type);
        file.setCreatedAt(LocalDateTime.now());

        return fileRepository.save(file);
    }

    @Transactional
    public List<File> findAll() {
        return fileRepository.findAll();
    }

    @Transactional
    public Optional<File> findById(Long id) {
        return fileRepository.findById(id);
    }

    @Transactional
    public void removeFile(Long id) {
        File file = fileRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Invalid file id: " + id));

        fileRepository.deleteById(id);
    }

    @Transactional
    public File updateFile(Long id, String type, String name) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid file id: " + id));

        file.setType(type);

        return fileRepository.save(file);
    }
}
