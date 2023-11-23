package me.code.dropfolder.exception.dto.detail;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

/**
 * A data class representing an error detail related to file upload failures.
 * It extends the ErrorDetail class and provides information specific to file upload errors.
 */
@Data
@Getter
@EqualsAndHashCode(callSuper = false)
public class FileUploadErrorDetail extends ErrorDetail {

    private final String rejectedFileName;
    private final String rejectedContentType;
    private final Long rejectedSize;
    private final String message;

    /**
     * Constructs a FileUploadErrorDetail with the specified rejected file information and exception details.
     *
     * @param file      The MultipartFile that was rejected during upload.
     * @param exception The exception associated with the file upload failure.
     */
    public FileUploadErrorDetail(MultipartFile file, Exception exception) {
        this.rejectedFileName = file.getOriginalFilename();
        this.rejectedContentType = file.getContentType();
        this.rejectedSize = file.getSize();
        this.message = exception.getLocalizedMessage();
    }
}
