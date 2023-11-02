package code.me.dropfolder.exception;

import code.me.dropfolder.exception.dto.Error;
import code.me.dropfolder.exception.type.AccountRegistrationException;
import code.me.dropfolder.exception.type.NonUniqueUsernameException;
import code.me.dropfolder.exception.type.PasswordFormattingException;
import code.me.dropfolder.exception.type.UsernameFormattingException;
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

    // Handles generic exceptions
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleException(Exception exception) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    // Handles exceptions related to user account registration
    @org.springframework.web.bind.annotation.ExceptionHandler(AccountRegistrationException.class)
    public ResponseEntity<Error> handlesAccountRegistrationException(RuntimeException exception) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    // Handles exceptions related to username formatting errors
    @org.springframework.web.bind.annotation.ExceptionHandler(UsernameFormattingException.class)
    public ResponseEntity<Error> handleUsernameFormattingException(RuntimeException exception) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, exception);
    }

    // Handles exceptions related to password formatting errors
    @org.springframework.web.bind.annotation.ExceptionHandler(PasswordFormattingException.class)
    public ResponseEntity<Error> handlePasswordFormattingException(RuntimeException exception) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, exception);
    }

    // Handles exceptions related to non-unique usernames
    @org.springframework.web.bind.annotation.ExceptionHandler(NonUniqueUsernameException.class)
    public ResponseEntity<Error> handleNonUniqueUsernameException(RuntimeException exception) {
        return buildResponseEntity(HttpStatus.CONFLICT, exception);
    }
}