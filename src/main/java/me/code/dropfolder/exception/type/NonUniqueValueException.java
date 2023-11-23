package me.code.dropfolder.exception.type;

import lombok.Getter;
import me.code.dropfolder.exception.dto.detail.ValidationErrorDetail;

/**
 * Exception class representing a failure due to a non-unique value violation.
 * This exception is thrown when an attempt is made to insert or update a value that must be unique, but the uniqueness constraint is violated.
 */
@Getter
public class NonUniqueValueException extends ValidationException {

    /**
     * Constructs a NonUniqueValueException with the specified detail message and validation error.
     *
     * @param message         the detail message.
     * @param validationError the validation error detail.
     */
    public NonUniqueValueException(String message, ValidationErrorDetail validationError) {
        super(message, validationError);
    }
}
