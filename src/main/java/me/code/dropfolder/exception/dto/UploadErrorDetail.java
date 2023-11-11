package me.code.dropfolder.exception.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

/**
 * A data class representing an upload error.
 * It extends the ErrorDetail class and provides additional information specific to file uploading errors.
 */
@Getter
@Data
@EqualsAndHashCode(callSuper = false)
public class UploadErrorDetail extends ErrorDetail {

    private String rejectedFileName;

    private String rejectedContentType;

    private Long rejectedSize;

    private String message;

    public UploadErrorDetail(MultipartFile file, Exception exception) {
        this.rejectedFileName = file.getOriginalFilename();
        this.rejectedContentType = file.getContentType();
        this.rejectedSize = file.getSize();
        this.message = exception.getLocalizedMessage();
    }
}
