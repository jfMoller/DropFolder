package me.code.dropfolder.exception.type;

import lombok.Getter;
import me.code.dropfolder.exception.dto.detail.ValidationErrorDetail;

/**
 * Base exception class for validation-related failures.
 * This exception is extended by more specific validation-related exceptions and provides details about the validation failure.
 */
@Getter
public class ValidationException extends RuntimeException {

    private final ValidationErrorDetail validationError;

    /**
     * Constructs a ValidationException with the specified detail message and validation error.
     *
     * @param message         the detail message.
     * @param validationError the validation error detail.
     */
    public ValidationException(String message, ValidationErrorDetail validationError) {
        super(message);
        this.validationError = validationError;
    }
}
