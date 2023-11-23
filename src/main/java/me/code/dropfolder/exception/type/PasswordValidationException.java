package me.code.dropfolder.exception.type;

import lombok.Getter;
import me.code.dropfolder.exception.dto.detail.ValidationErrorDetail;

/**
 * Exception class representing a failure in password validation.
 * This exception is thrown when a password validation error occurs, and it contains details about the validation failure.
 */
@Getter
public class PasswordValidationException extends ValidationException {

    /**
     * Constructs a PasswordValidationException with the specified detail message and validation error.
     *
     * @param message         the detail message.
     * @param validationError the validation error detail.
     */
    public PasswordValidationException(String message, ValidationErrorDetail validationError) {
        super(message, validationError);
    }
}
