package me.code.dropfolder.exception.type;

import me.code.dropfolder.exception.dto.detail.FileOperationErrorDetail;

/**
 * Exception class representing a failure in deleting a file.
 * This exception is thrown when there is an issue during the file deletion operation and includes details about the error.
 */
public class FileDeletionFailureException extends FileOperationException {

    /**
     * Constructs a FileDeletionFailureException with the specified detail message and deletion error detail.
     *
     * @param message       the detail message.
     * @param deletionError the detailed information about the error during file deletion.
     */
    public FileDeletionFailureException(String message, FileOperationErrorDetail deletionError) {
        super(message, deletionError);
    }
}
