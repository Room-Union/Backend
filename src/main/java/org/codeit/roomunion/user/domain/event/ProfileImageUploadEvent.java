package org.codeit.roomunion.user.domain.event;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ProfileImageUploadEvent {

    private final String filePath;
    private final MultipartFile file;

    private ProfileImageUploadEvent(String filePath, MultipartFile file) {
        this.filePath = filePath;
        this.file = file;
    }

    public static ProfileImageUploadEvent of(String filePath, MultipartFile file) {
        return new ProfileImageUploadEvent(filePath, file);
    }
}
