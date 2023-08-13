package org.zerock.moamoa.utils.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.exception.FileProcessingException;
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
import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
class FileUtils {
    public static Path root;
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");


    public static void createDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException ie) {
            throw new FileProcessingException(ErrorCode.FILE_STORAGE_FAILED, "디렉토리 생성 실패");
        }
    }

    /**
     * 파일 저장
     */
    public static FileResponse saveFile(MultipartFile file, String folder, String fileName) {
        getPath(folder);
        if (fileName == null) fileName = validateFileName(file.getOriginalFilename());

        String timeStamp = dateFormat.format(new Date());
        fileName = timeStamp + "_" + fileName;
        String ext = getExt(Objects.requireNonNull(file.getOriginalFilename()));
        validateFileSize(file.getSize());
        Path filePath = save(file, fileName + "." + ext);
        return buildFileResponse(filePath, file.getSize(), file.getContentType());
    }

    /**
     * 입력된 folder와 기본 경로를 통해 디렉터리를 생성
     */
    private static void getPath(String folder) {
        String defaultPath = FileConfig.getPath();
        root = Paths.get(defaultPath + folder);
        createDirectory(root);
    }

    /**
     * 확장자 가져오기
     */
    static String getExt(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    //저장
    private static Path save(MultipartFile file, String fileName) {
        Path targetLocation = root.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ie) {
            throw new FileProcessingException(ErrorCode.FILE_UPLOAD_FAILED);
        }
        return targetLocation;
    }

    static void remove(String filename, String folder) {
        String pathname = FileConfig.getPath() + folder + File.separator + filename;
        log.info(pathname);
        //UUID가 포함된 파일이름을 디코딩해줍니다.
        File file = new File(pathname);
        boolean result = file.delete();
    }


    private static FileResponse buildFileResponse(Path filePath, long size, String contentType) {
        return FileResponse.builder()
                .fileName(filePath.getFileName().toString())
                .fileSize(size)
                .contentType(contentType)
                .uploadTimeStamp(Instant.now())
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
            throw new FileProcessingException(ErrorCode.FILE_SIZE_ZERO);
        }
    }

}
