package me.code.dropfolder.exceptions.types;

import lombok.Getter;
import me.code.dropfolder.exceptions.dtos.details.ValidationErrorDetail;

/**
 * Base exceptions class for validation-related failures.
 * This exceptions is extended by more specific validation-related exceptions and provides details about the validation failure.
 */
@Getter
public class ValidationException extends RuntimeException {

    private final ValidationErrorDetail validationError;

    /**
     * Constructs a ValidationException with the specified details message and validation error.
     *
     * @param message         the details message.
     * @param validationError the validation error details.
     */
    public ValidationException(String message, ValidationErrorDetail validationError) {
        super(message);
        this.validationError = validationError;
    }
}
