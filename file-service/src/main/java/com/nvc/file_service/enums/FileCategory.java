package com.nvc.file_service.enums;

import lombok.Getter;

@Getter
public enum FileCategory {
    AVATAR("avatar"),
    MOTEL_IMAGE("motel-image"),
    POST_IMAGE("post-image");

    FileCategory(String folderName) {
        this.folderName=folderName;
    }

    private String folderName;
}
