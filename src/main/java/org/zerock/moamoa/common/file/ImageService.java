package org.zerock.moamoa.common.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.exception.InvalidValueException;
import org.zerock.moamoa.common.file.dto.FileResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService extends FileSerivce {
    public ImageService() {
        this("");
    }

    public ImageService(String uploadDir) {
        super(uploadDir);
    }

    public List<FileResponse> saveFiles(MultipartFile[] files, Long id) {
        List<FileResponse> responses = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            responses.add(saveFile(files[i], id + "_" + i));
        }
        return responses;
    }

    @Override
    public FileResponse saveFile(MultipartFile file, String fileName) {

        if (validImageExtension(file.getOriginalFilename())) {
            return super.saveFile(file, fileName);
        } else throw new InvalidValueException(ErrorCode.FILE_NOT_VALID_EXTENSION);

    }

    public boolean validImageExtension(String originalFilename) {
        String ext = getExt(originalFilename);
        boolean check = false;
        for (String extension : Extensions.IMAGE.getExtensions()) {
            if (extension.equals(ext)) check = true;
        }
        return check;
    }
}
