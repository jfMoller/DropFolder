package me.code.dropfolder.exceptions.types;

import lombok.Getter;
import me.code.dropfolder.exceptions.dtos.details.FileOperationErrorDetail;

/**
 * Exception class representing a generic failure in a file operation.
 * This exceptions is thrown when there is an issue during file-related operations and includes details about the error.
 */
@Getter
public class FileOperationException extends RuntimeException {

    private final FileOperationErrorDetail fileOperationErrorDetail;

    /**
     * Constructs a FileOperationException with the specified details message and file operation error details.
     *
     * @param message                  the details message.
     * @param fileOperationErrorDetail the detailed information about the error during the file operation.
     */
    public FileOperationException(String message, FileOperationErrorDetail fileOperationErrorDetail) {
        super(message);
        this.fileOperationErrorDetail = fileOperationErrorDetail;
    }
}
