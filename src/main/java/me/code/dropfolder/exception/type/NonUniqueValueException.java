package me.code.dropfolder.exception.type;

import me.code.dropfolder.exception.dto.ValidationError;

public class NonUniqueValueException extends RuntimeException {
    private ValidationError validationError;

    public NonUniqueValueException(String message, ValidationError validationError) {
        super(message);
        this.validationError = validationError;
    }

    public ValidationError getValidationError() {
        return validationError;
    }
}
