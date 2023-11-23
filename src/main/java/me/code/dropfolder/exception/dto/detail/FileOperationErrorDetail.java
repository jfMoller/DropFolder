package me.code.dropfolder.exception.dto.detail;

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
public class FileOperationErrorDetail extends ErrorDetail {

    private String rejectedFileName;

    private String rejectedContentType;

    private Long rejectedSize;

    private String message;

    /**
     * Constructs an instance of FileOperationErrorDetail based on the given MultipartFile and exception.
     *
     * @param file      The MultipartFile that caused the upload error.
     * @param exception The exception associated with the upload error.
     */
    public FileOperationErrorDetail(MultipartFile file, Exception exception) {
        this.rejectedFileName = file.getOriginalFilename();
        this.rejectedContentType = file.getContentType();
        this.rejectedSize = file.getSize();
        this.message = exception.getLocalizedMessage();
    }
}
