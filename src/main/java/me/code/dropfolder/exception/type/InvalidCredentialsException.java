package me.code.dropfolder.exception.type;

import lombok.Getter;
import me.code.dropfolder.exception.dto.detail.ValidationErrorDetail;

/**
 * Exception class representing invalid login credentials.
 * Extends ValidationException and includes details about the validation error.
 */
@Getter
public class InvalidCredentialsException extends ValidationException {

    /**
     * Constructs an InvalidCredentialsException with the specified detail message and validation error details.
     *
     * @param message         the detail message.
     * @param validationError details about the validation error.
     */
    public InvalidCredentialsException(String message, ValidationErrorDetail validationError) {
        super(message, validationError);
    }
}
