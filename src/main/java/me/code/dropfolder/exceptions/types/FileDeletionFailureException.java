package me.code.dropfolder.exceptions.types;

import me.code.dropfolder.exceptions.dtos.details.FileOperationErrorDetail;

/**
 * Exception class representing a failure in deleting a file.
 * This exceptions is thrown when there is an issue during the file deletion operation and includes details about the error.
 */
public class FileDeletionFailureException extends FileOperationException {

    /**
     * Constructs a FileDeletionFailureException with the specified details message and deletion error details.
     *
     * @param message       the details message.
     * @param deletionError the detailed information about the error during file deletion.
     */
    public FileDeletionFailureException(String message, FileOperationErrorDetail deletionError) {
        super(message, deletionError);
    }
}
