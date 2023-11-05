package me.code.dropfolder.exception.type;

import lombok.Getter;
import me.code.dropfolder.exception.dto.ValidationErrorDetail;

@Getter
public class UsernameFormattingException extends RuntimeException {
    private ValidationErrorDetail validationError;

    public UsernameFormattingException(String message, ValidationErrorDetail validationError) {
        super(message);
        this.validationError = validationError;
    }

}
