package me.code.dropfolder.exception.type;

import lombok.Getter;
import me.code.dropfolder.exception.dto.ValidationErrorDetail;

@Getter
public class PasswordValidationException extends ValidationException {

    public PasswordValidationException(String message, ValidationErrorDetail validationError) {
        super(message, validationError);
    }

}
