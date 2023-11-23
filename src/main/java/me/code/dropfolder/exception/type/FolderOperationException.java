package me.code.dropfolder.exception.type;

import lombok.Getter;
import me.code.dropfolder.exception.dto.detail.FolderOperationErrorDetail;

/**
 * Exception class representing a generic failure in a folder operation.
 * This exception is thrown when there is an issue during folder-related operations and includes details about the error.
 */
@Getter
public class FolderOperationException extends RuntimeException {

    private final FolderOperationErrorDetail folderOperationErrorDetail;

    /**
     * Constructs a FolderOperationException with the specified detail message and folder operation error detail.
     *
     * @param message                    the detail message.
     * @param folderOperationErrorDetail the detailed information about the error during the folder operation.
     */
    public FolderOperationException(String message, FolderOperationErrorDetail folderOperationErrorDetail) {
        super(message);
        this.folderOperationErrorDetail = folderOperationErrorDetail;
    }
}
