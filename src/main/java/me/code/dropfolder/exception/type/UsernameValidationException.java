package me.code.dropfolder.exception.type;

import lombok.Getter;
import me.code.dropfolder.exception.dto.detail.ValidationErrorDetail;

/**
 * Exception class representing a failure in username validation.
 * This exception is thrown when a username validation error occurs, and it contains details about the validation failure.
 */
@Getter
public class UsernameValidationException extends ValidationException {

    /**
     * Constructs a UsernameValidationException with the specified detail message and validation error.
     *
     * @param message         the detail message.
     * @param validationError the validation error detail.
     */
    public UsernameValidationException(String message, ValidationErrorDetail validationError) {
        super(message, validationError);
    }
}
