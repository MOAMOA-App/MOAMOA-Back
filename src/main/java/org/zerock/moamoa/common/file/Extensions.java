package org.zerock.moamoa.common.file;

import lombok.Getter;

@Getter
public enum Extensions {
    IMAGE(new String[]{"png", "jpeg", "jpg"});

    private final String[] extensions;

    Extensions(String[] extensions) {
        this.extensions = extensions;
    }
}
