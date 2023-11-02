package code.me.dropfolder.exception;

import code.me.dropfolder.exception.dto.Error;
import code.me.dropfolder.exception.type.RegistrationFormattingException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Error> buildResponseEntity(HttpStatus status, Throwable exception) {
        return new Error(status, exception).toResponseEntity();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleException(Exception exception) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RegistrationFormattingException.class)
    public ResponseEntity<Error> handleRegistrationFormattingException(RegistrationFormattingException exception) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, exception);
    }
}