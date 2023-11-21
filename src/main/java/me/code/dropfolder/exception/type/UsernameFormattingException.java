package me.code.dropfolder.exception.type;

import lombok.Getter;
import me.code.dropfolder.exception.dto.ValidationErrorDetail;

@Getter
public class UsernameFormattingException extends FormattingException {

    public UsernameFormattingException(String message, ValidationErrorDetail validationError) {
        super(message, validationError);
    }

}
