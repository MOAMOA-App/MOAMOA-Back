package org.zerock.moamoa.api.file;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.exception.FileException;
import org.zerock.moamoa.common.exception.InvalidValueException;

@Service
public abstract class FileService {
	private final String uploadDir = "${file.upload.path}";
	private final Path root;

	public FileService(@Value("/") String uploadPath) {
		this.root = Paths.get(uploadDir + uploadPath);
		try {
			Files.createDirectories(root);
		} catch (IOException ie) {
			throw new FileException(ErrorCode.FILE_STORAGE_FAILED, "디렉토리 생성 실패");
		}
	}

	public abstract boolean checkType();

	public FileResponse store(MultipartFile file, String name, int numb) {
		String fileName = settingFileName(name, numb);
		validateFileSize(file.getSize());
		Path filePath = saveFile(file, fileName);
		return buildFileResponse(filePath, file.getSize(), file.getContentType());
	}

	public FileResponse store(MultipartFile file) {
		String fileName = validateFileName(file.getOriginalFilename());
		validateFileSize(file.getSize());
		Path filePath = saveFile(file, fileName);
		return buildFileResponse(filePath, file.getSize(), file.getContentType());
	}

	/** 파일 저장 */
	private Path saveFile(MultipartFile file, String fileName) {
		Path targetLocation = root.resolve(fileName);
		try {
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ie) {
			throw new FileException(ErrorCode.FILE_UPLOAD_FAILED);
		}
		return targetLocation;
	}

	/** 파일 출력 */
	public Resource load(String fileName) {
		Path filePath = root.resolve(fileName);
		Resource resource;
		try {
			resource = new UrlResource(filePath.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			throw new FileException(ErrorCode.FILE_NOT_FOUND, "파일 명 : " + fileName);
		} catch (MalformedURLException ex) {
			throw new FileException(ErrorCode.FILE_NOT_FOUND, "파일 명 : " + fileName);
		}
	}

	private String getFileExtention(String originalFileName) {
		int dotIndex = originalFileName.lastIndexOf(".");
		return dotIndex < 0 ? "" : originalFileName.substring(dotIndex);
	}

	/** file 응답 객체 생성 */
	private FileResponse buildFileResponse(Path filePath, long fileSize, String contentType) {
		return FileResponse.builder()
			.fileName(filePath.getFileName().toString())
			.fileSize(fileSize)
			.contentType(contentType)
			.uploadTimeStamp(LocalDateTime.now())
			.build();
	}

	/** 파일 명 설정 */
	private String settingFileName(String name, int numb) {
		return StringUtils.cleanPath(name + "_" + numb);
	}

	/** 파일 명 검증 */
	private String validateFileName(String originalFileName) {
		if (originalFileName == null || originalFileName.isEmpty()) {
			throw new InvalidValueException(ErrorCode.INVALID_INPUT_VALUE, "잘못된 요청 값 입니다.");
		}
		return StringUtils.cleanPath(originalFileName);
	}

	/** 파일 크기 검증 */
	private void validateFileSize(long fileSize) {
		if (fileSize == 0) {
			throw new FileException(ErrorCode.FILE_SIZE_ZERO);
		}
	}

}
