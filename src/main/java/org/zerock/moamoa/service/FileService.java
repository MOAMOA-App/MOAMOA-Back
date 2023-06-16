package org.zerock.moamoa.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.domain.entity.File;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.FileRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FileService {
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public File saveFile(User user, String type, String name) throws NotFoundException {
        File file = new File();
        file.setUser(user);
        file.setType(type);
        file.setName(name);
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
        // 주어진 ID에 해당하는 파일을 삭제
        // 요청한 ID에 해당하는 파일 없을 경우 예외 발생
        File file = fileRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Invalid file id: " + id));

        fileRepository.deleteById(id);
    }

    @Transactional
    public File updateFile(Long id, String type, String name) {
        // 주어진 ID에 해당하는 파일의 정보(type 및 name)를 업데이트
        // 요청한 ID에 해당하는 파일 없을 경우 예외 발생
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid file id: " + id));

        file.setType(type);
        file.setName(name);

        return fileRepository.save(file);
    }
}
