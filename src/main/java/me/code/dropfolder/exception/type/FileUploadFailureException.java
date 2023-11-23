package me.code.dropfolder.exception.type;

import lombok.Getter;
import me.code.dropfolder.exception.dto.detail.FileOperationErrorDetail;

/**
 * Exception class representing a failure in uploading a file.
 */
@Getter
public class FileUploadFailureException extends FileOperationException {

    /**
     * Constructs a FileUploadFailureException with the specified detail message and upload error details.
     *
     * @param message     the detail message.
     * @param uploadError the details of the upload error.
     */
    public FileUploadFailureException(String message, FileOperationErrorDetail uploadError) {
        super(message, uploadError);
    }
}