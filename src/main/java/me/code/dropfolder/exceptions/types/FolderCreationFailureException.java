package me.code.dropfolder.exceptions.types;

import me.code.dropfolder.exceptions.dtos.details.FolderOperationErrorDetail;

/**
 * Exception class representing a failure in creating a folder.
 * This exceptions is thrown when there is an issue during the creation of a folder and includes details about the error.
 */
public class FolderCreationFailureException extends FolderOperationException {

    /**
     * Constructs a FolderCreationFailureException with the specified details message and folder creation error details.
     *
     * @param message       the details message.
     * @param creationError the detailed information about the error during folder creation.
     */
    public FolderCreationFailureException(String message, FolderOperationErrorDetail creationError) {
        super(message, creationError);
    }
}
