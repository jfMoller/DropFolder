package me.code.dropfolder.exceptions.dtos.details;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A class used to represent details of an error related to folder operations.
 * It extends the {@link ErrorDetail} class and includes specific information about the rejected folder and an error message.
 */
@Getter
@Data
@EqualsAndHashCode(callSuper = false)
public class FolderOperationErrorDetail extends ErrorDetail {

    private String rejectedFolderName;

    private String message;

    /**
     * Constructs a new instance with the specified rejected folder name and error message.
     *
     * @param rejectedFolderName The name of the folder that was rejected.
     * @param message            The error message providing details about the error.
     */
    public FolderOperationErrorDetail(String rejectedFolderName, String message) {
        this.rejectedFolderName = rejectedFolderName;
        this.message = message;
    }
}
