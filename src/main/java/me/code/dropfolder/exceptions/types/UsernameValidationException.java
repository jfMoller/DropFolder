package me.code.dropfolder.exceptions.types;

import lombok.Getter;
import me.code.dropfolder.exceptions.dtos.details.ValidationErrorDetail;

/**
 * Exception class representing a failure in username validation.
 * This exceptions is thrown when a username validation error occurs; it provides details about the validation failure.
 */
@Getter
public class UsernameValidationException extends ValidationException {

    /**
     * Constructs a UsernameValidationException with the specified details message and validation error.
     *
     * @param message         the details message.
     * @param validationError the validation error details.
     */
    public UsernameValidationException(String message, ValidationErrorDetail validationError) {
        super(message, validationError);
    }
}
