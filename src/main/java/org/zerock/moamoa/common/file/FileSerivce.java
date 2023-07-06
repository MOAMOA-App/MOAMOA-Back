package org.zerock.moamoa.common.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.exception.FileProcessingException;
import org.zerock.moamoa.common.exception.InvalidValueException;
import org.zerock.moamoa.common.file.dto.FileResponse;
import org.zerock.moamoa.config.FileConfig;

@Service
public class FileSerivce {
	public final Path root;

	public FileSerivce() {
		this("");
	}

	public FileSerivce(String uploadDir) {
		String defaultPath = FileConfig.getPath();
		this.root = Paths.get(defaultPath + uploadDir);
		createDirectory(root);
	}

	public void createDirectory(Path path) {
		try {
			Files.createDirectories(path);
		} catch (IOException ie) {
			throw new FileProcessingException(ErrorCode.FILE_STORAGE_FAILED, "디렉토리 생성 실패");
		}
	}

	public FileResponse saveFile(MultipartFile file, String fileName) {
		if (fileName == null)
			fileName = validateFileName(file.getOriginalFilename());
		String ext = getExt(file.getOriginalFilename());
		validateFileSize(file.getSize());
		Path filePath = save(file, fileName + "." + ext);
		return buildFileResponse(filePath, file.getSize(), file.getContentType());
	}

	String getExt(String filename) {
		return filename.substring(filename.lastIndexOf(".") + 1);
	}

	private Path save(MultipartFile file, String fileName) {
		Path targetLocation = root.resolve(fileName);
		try {
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ie) {
			throw new FileProcessingException(ErrorCode.FILE_UPLOAD_FAILED);
		}
		return targetLocation;
	}

	private FileResponse buildFileResponse(Path filePath, long size, String contentType) {
		return FileResponse.builder()
			.fileName(filePath.getFileName().toString())
			.fileSize(size)
			.contentType(contentType)
			.uploadTimeStamp(LocalDateTime.now())
			.build();
	}

	private String validateFileName(String originalFileName) {
		if (originalFileName == null || originalFileName.isEmpty()) {
			throw new InvalidValueException(ErrorCode.INVALID_INPUT_VALUE, "잘못된 요청 값 입니다.");
		}
		return StringUtils.cleanPath(originalFileName);
	}

	private void validateFileSize(long fileSize) {
		if (fileSize == 0) {
			throw new FileProcessingException(ErrorCode.FILE_SIZE_ZERO);
		}
	}

}
