package org.zerock.moamoa.api.file;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageService extends FileService {
	private final static String PATH = "/image";

	public ImageService(@Value("") String uploadPath) {
		super(PATH + uploadPath);
	}

	@Override
	public boolean checkType() {
		return false;
	}

	public List<FileResponse> saveImages(MultipartFile[] images, Long id) {
		return
			IntStream
				.range(0, images.length)
				.mapToObj(i -> store(images[i], String.valueOf(id), i))
				.toList();
	}

	public FileResponse saveImage(MultipartFile image, Long id) {

		return store(image, String.valueOf(id), 0);
	}

}