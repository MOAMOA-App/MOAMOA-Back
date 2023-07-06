package org.zerock.moamoa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileConfig {
	private static String fileUploadPath;

	@Value("${file.upload.path}")
	public void setPath(String path) {
		fileUploadPath = path;
	}

	public static String getPath() {
		return fileUploadPath;
	}

}