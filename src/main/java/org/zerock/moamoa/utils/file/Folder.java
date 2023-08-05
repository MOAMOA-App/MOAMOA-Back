package org.zerock.moamoa.utils.file;

import lombok.Getter;

@Getter
public enum Folder {
    PROFILE("/profile/"),
    PRODUCT("/product/");

    private final String folder;

    Folder(String folder) {
        this.folder = folder;
    }
}
