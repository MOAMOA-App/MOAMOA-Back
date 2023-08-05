package org.zerock.moamoa.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "file.upload")
public class FileConfig {
    private static String path;
    private static String url;


    public static String getPath() {
        return path;
    }

    public void setPath(String dir) {
        this.path = dir;
    }

    public static String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}