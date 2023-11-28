package me.code.dropfolder.exceptions.types;

import lombok.Getter;
import me.code.dropfolder.exceptions.dtos.details.ValidationErrorDetail;

/**
 * Exception class representing a failure in password validation.
 * This exceptions is thrown when a password validation error occurs, and it contains details about the validation failure.
 */
@Getter
public class PasswordValidationException extends ValidationException {

    /**
     * Constructs a PasswordValidationException with the specified details message and validation error.
     *
     * @param message         the details message.
     * @param validationError the validation error details.
     */
    public PasswordValidationException(String message, ValidationErrorDetail validationError) {
        super(message, validationError);
    }
}
