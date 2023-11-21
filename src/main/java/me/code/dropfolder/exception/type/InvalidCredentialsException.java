package me.code.dropfolder.exception.type;

import lombok.Getter;
import me.code.dropfolder.exception.dto.ValidationErrorDetail;

@Getter
public class InvalidCredentialsException extends FormattingException {

    public InvalidCredentialsException(String message, ValidationErrorDetail validationError) {
        super(message, validationError);
    }

}