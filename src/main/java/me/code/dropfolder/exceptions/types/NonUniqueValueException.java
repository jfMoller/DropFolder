package me.code.dropfolder.exceptions.types;

import lombok.Getter;
import me.code.dropfolder.exceptions.dtos.details.ValidationErrorDetail;

/**
 * Exception class representing a failure due to a non-unique value violation.
 * This exceptions is thrown when an attempt is made to insert or update a value that must be unique, but the uniqueness constraint is violated.
 */
@Getter
public class NonUniqueValueException extends ValidationException {

    /**
     * Constructs a NonUniqueValueException with the specified details message and validation error.
     *
     * @param message         the details message.
     * @param validationError the validation error details.
     */
    public NonUniqueValueException(String message, ValidationErrorDetail validationError) {
        super(message, validationError);
    }
}
