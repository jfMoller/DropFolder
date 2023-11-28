package me.code.dropfolder.exceptions.types;

import lombok.Getter;
import me.code.dropfolder.exceptions.dtos.details.ValidationErrorDetail;

/**
 * Exception class representing invalid login credentials.
 * Extends ValidationException and includes details about the validation error.
 */
@Getter
public class InvalidCredentialsException extends ValidationException {

    /**
     * Constructs an InvalidCredentialsException with the specified details message and validation error details.
     *
     * @param message         the details message.
     * @param validationError details about the validation error.
     */
    public InvalidCredentialsException(String message, ValidationErrorDetail validationError) {
        super(message, validationError);
    }
}
