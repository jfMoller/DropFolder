package me.code.dropfolder.exception.type;

import lombok.Getter;
import me.code.dropfolder.exception.dto.detail.FileOperationErrorDetail;

/**
 * Exception class representing a generic failure in a file operation.
 * This exception is thrown when there is an issue during file-related operations and includes details about the error.
 */
@Getter
public class FileOperationException extends RuntimeException {

    private final FileOperationErrorDetail fileOperationErrorDetail;

    /**
     * Constructs a FileOperationException with the specified detail message and file operation error detail.
     *
     * @param message                  the detail message.
     * @param fileOperationErrorDetail the detailed information about the error during the file operation.
     */
    public FileOperationException(String message, FileOperationErrorDetail fileOperationErrorDetail) {
        super(message);
        this.fileOperationErrorDetail = fileOperationErrorDetail;
    }
}
