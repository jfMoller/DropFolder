package me.code.dropfolder.exception.type;

import me.code.dropfolder.exception.dto.ValidationError;

public class UsernameFormattingException extends RuntimeException {
    private ValidationError validationError;

    public UsernameFormattingException(String message, ValidationError validationError) {
        super(message);
        this.validationError = validationError;
    }

    public ValidationError getValidationError() {
        return validationError;
    }
}
