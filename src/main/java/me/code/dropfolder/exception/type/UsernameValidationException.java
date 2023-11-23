package me.code.dropfolder.exception.type;

import lombok.Getter;
import me.code.dropfolder.exception.dto.ValidationErrorDetail;

@Getter
public class UsernameValidationException extends ValidationException {

    public UsernameValidationException(String message, ValidationErrorDetail validationError) {
        super(message, validationError);
    }

}
