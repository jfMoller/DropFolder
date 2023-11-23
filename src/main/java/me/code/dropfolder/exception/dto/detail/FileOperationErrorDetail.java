package me.code.dropfolder.exception.dto.detail;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A data class representing an error detail related to file operations.
 * It extends the ErrorDetail class and provides additional information specific to file operation errors.
 */
@Getter
@Data
@EqualsAndHashCode(callSuper = false)
public class FileOperationErrorDetail extends ErrorDetail {

    private String message;

    /**
     * Constructs a FileOperationErrorDetail with the specified error message.
     *
     * @param message The error message describing the file operation error.
     */
    public FileOperationErrorDetail(String message) {
        this.message = message;
    }
}
