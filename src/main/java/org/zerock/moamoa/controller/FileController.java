package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.moamoa.config.FileConfig;
import org.zerock.moamoa.service.FileService;
import org.zerock.moamoa.utils.file.dto.FileRequest;
import org.zerock.moamoa.utils.file.dto.FileResultResponse;

@RestController
@Slf4j
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    /**
     * 이미지 불러오기 (경로 지정)
     */
    @GetMapping("{category}/{file}")
    public Resource getImage(@PathVariable("category") String category, @PathVariable("file") String file) {
        String folder = FileConfig.findPath(category);

        return new FileSystemResource(folder + "/" + file);
    }

    /**
     * 이미지 저장하기
     */
    @PostMapping("")
    public FileResultResponse saveImage(Authentication authentication, @ModelAttribute FileRequest request, MultipartFile[] images) {
        return fileService.saveFile(request, images, authentication.getPrincipal().toString());
    }
}