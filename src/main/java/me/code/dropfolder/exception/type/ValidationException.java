package me.code.dropfolder.exception.type;

import lombok.Getter;
import me.code.dropfolder.exception.dto.detail.ValidationErrorDetail;

@Getter
public class ValidationException extends RuntimeException {
    private ValidationErrorDetail validationError;

    public ValidationException(String message, ValidationErrorDetail validationError) {
        super(message);
        this.validationError = validationError;
    }

}
