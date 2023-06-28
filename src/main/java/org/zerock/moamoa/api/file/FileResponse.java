package org.zerock.moamoa.api.file;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
public class FileResponse {
	private String fileName;
	private long fileSize;
	private String contentType;
	private LocalDateTime uploadTimeStamp;

	@Builder
	public FileResponse(String fileName, long fileSize, String contentType, LocalDateTime uploadTimeStamp) {
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.contentType = contentType;
		this.uploadTimeStamp = uploadTimeStamp;
	}
}
