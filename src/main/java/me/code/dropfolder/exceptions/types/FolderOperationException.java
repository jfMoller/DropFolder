package me.code.dropfolder.exceptions.types;

import lombok.Getter;
import me.code.dropfolder.exceptions.dtos.details.FolderOperationErrorDetail;

/**
 * Exception class representing a generic failure in a folder operation.
 * This exceptions is thrown when there is an issue during folder-related operations and includes details about the error.
 */
@Getter
public class FolderOperationException extends RuntimeException {

    private final FolderOperationErrorDetail folderOperationErrorDetail;

    /**
     * Constructs a FolderOperationException with the specified details message and folder operation error details.
     *
     * @param message                    the details message.
     * @param folderOperationErrorDetail the detailed information about the error during the folder operation.
     */
    public FolderOperationException(String message, FolderOperationErrorDetail folderOperationErrorDetail) {
        super(message);
        this.folderOperationErrorDetail = folderOperationErrorDetail;
    }
}
