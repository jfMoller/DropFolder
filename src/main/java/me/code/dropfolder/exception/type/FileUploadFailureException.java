package me.code.dropfolder.exception.type;

import lombok.Getter;
import me.code.dropfolder.exception.dto.detail.FileUploadErrorDetail;

/**
 * Exception class representing a failure in file upload operations.
 * It extends the RuntimeException class and includes details about the file upload error.
 */
@Getter
public class FileUploadFailureException extends RuntimeException {

    private final FileUploadErrorDetail uploadError;

    /**
     * Constructs a FileUploadFailureException with the specified error message and file upload error details.
     *
     * @param message     A message describing the file upload failure.
     * @param uploadError The details of the file upload failure.
     */
    public FileUploadFailureException(String message, FileUploadErrorDetail uploadError) {
        super(message);
        this.uploadError = uploadError;
    }
}
