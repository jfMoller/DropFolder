package me.code.dropfolder.exception.type;

import me.code.dropfolder.exception.dto.detail.FolderOperationErrorDetail;

/**
 * Exception class representing a failure in creating a folder.
 * This exception is thrown when there is an issue during the creation of a folder and includes details about the error.
 */
public class FolderCreationFailureException extends FolderOperationException {

    /**
     * Constructs a FolderCreationFailureException with the specified detail message and folder creation error detail.
     *
     * @param message       the detail message.
     * @param creationError the detailed information about the error during folder creation.
     */
    public FolderCreationFailureException(String message, FolderOperationErrorDetail creationError) {
        super(message, creationError);
    }
}
