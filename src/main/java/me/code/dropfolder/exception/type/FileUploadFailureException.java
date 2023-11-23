package me.code.dropfolder.exception.type;

import lombok.Getter;
import me.code.dropfolder.exception.dto.detail.UploadErrorDetail;

@Getter
public class FileUploadFailureException extends RuntimeException {
    private UploadErrorDetail uploadError;

    public FileUploadFailureException(String message, UploadErrorDetail uploadError) {
        super(message);
        this.uploadError = uploadError;
    }

}
