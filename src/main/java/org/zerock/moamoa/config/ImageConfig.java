package org.zerock.moamoa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageConfig {

    @Value("${image.upload.path}")
    private String imageUploadPath;

    public String getImageUploadPath() {
        return imageUploadPath;
    }
}
