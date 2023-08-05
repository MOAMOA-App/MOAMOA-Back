package org.zerock.moamoa.utils.file;

import lombok.Getter;

@Getter
public enum Extensions {
    IMAGE(new String[]{"png", "jpeg", "jpg", "PNG", "JPEG", "JPG"});

    private final String[] extensions;

    Extensions(String[] extensions) {
        this.extensions = extensions;
    }
}
