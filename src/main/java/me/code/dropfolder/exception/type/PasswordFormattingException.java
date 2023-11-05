package me.code.dropfolder.exception.type;

import lombok.Getter;
import me.code.dropfolder.exception.dto.ValidationErrorDetail;

@Getter
public class PasswordFormattingException extends RuntimeException {
    private ValidationErrorDetail validationError;

    public PasswordFormattingException(String message, ValidationErrorDetail validationError) {
        super(message);
        this.validationError = validationError;
    }

}
