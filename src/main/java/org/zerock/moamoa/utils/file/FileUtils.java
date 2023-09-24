package org.zerock.moamoa.utils.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.exception.FileException;
import org.zerock.moamoa.common.exception.InvalidValueException;
import org.zerock.moamoa.config.FileConfig;
import org.zerock.moamoa.utils.file.dto.FileResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
class FileUtils {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

    public static void createDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException ie) {
            throw new FileException(ErrorCode.FILE_STORAGE_FAILED, "디렉토리 생성 실패");
        }
    }

    /**
     * 파일 저장
     */
    public static FileResponse saveFile(MultipartFile file, String category) {
        Path path = getPath(category);

        String fileName = System.currentTimeMillis() + "-" + UUID.randomUUID();

        String ext = getExt(Objects.requireNonNull(file.getOriginalFilename()));
        validateFileSize(file.getSize());
        Path filePath = save(file, path, fileName + "." + ext);
        return buildFileResponse(filePath, file.getSize(), file.getContentType(), category);
    }

    /**
     * 경로 탐색 후 디렉터리를 생성
     */
    private static Path getPath(String category) {
        String path = FileConfig.findPath(category);
        Path root = Paths.get(path);
        createDirectory(root);
        return root;
    }

    /**
     * 확장자 가져오기
     */
    static String getExt(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    //저장
    private static Path save(MultipartFile file, Path path, String fileName) {
        Path targetLocation = path.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return targetLocation;
    }

    static void remove(String filename) {
        String[] splits = filename.split("/");
        String path = FileConfig.findPath(splits[2]);
        String pathname = path + File.separator + splits[3];
        //UUID가 포함된 파일이름을 디코딩해줍니다.
        File file = new File(pathname);
        boolean result = file.delete();
    }


    private static FileResponse buildFileResponse(Path filePath, long size, String contentType, String category) {
        String fileRoot = String.valueOf(filePath).substring(1);
        fileRoot = fileRoot.replace("\\", "/");
        String path = FileConfig.getUrl() + "/" + category + "/";
        return FileResponse.builder()
                .fileName(path + filePath.getFileName().toString())
                .fileSize(size)
                .contentType(contentType)
                .uploadTimeStamp(Instant.now())
                .fileRoot(fileRoot)
                .build();
    }

    //파일 명 검증
    private static String validateFileName(String originalFileName) {
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_VALUE, "잘못된 요청 값 입니다.");
        }
        return StringUtils.cleanPath(originalFileName);
    }

    //파일 사이즈 검증
    private static void validateFileSize(long fileSize) {
        if (fileSize == 0) {
            throw new FileException(ErrorCode.FILE_SIZE_ZERO);
        }
    }

}