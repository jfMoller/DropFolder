package me.code.dropfolder.exception;

import me.code.dropfolder.exception.dto.Error;
import me.code.dropfolder.exception.dto.ErrorDetail;
import me.code.dropfolder.exception.dto.ValidationErrorDetail;
import me.code.dropfolder.exception.type.*;
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

    private <T extends ErrorDetail> ResponseEntity<Error> buildResponseEntity(HttpStatus status, Throwable exception, T errorDetail) {
        Error error = new Error(status, exception);
        error.addErrorDetail(errorDetail);
        return error.toResponseEntity();
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
    public ResponseEntity<Error> handleUsernameFormattingException(UsernameFormattingException exception) {
        ValidationErrorDetail validationError = exception.getValidationError();
        return buildResponseEntity(HttpStatus.CONFLICT, exception, validationError);
    }

    // Handles exceptions related to password formatting errors
    @org.springframework.web.bind.annotation.ExceptionHandler(PasswordFormattingException.class)
    public ResponseEntity<Error> handlePasswordFormattingException(PasswordFormattingException exception) {
        ValidationErrorDetail validationError = exception.getValidationError();
        return buildResponseEntity(HttpStatus.CONFLICT, exception, validationError);
    }

    // Handles exceptions related to non-unique values in the context of user registration
    @org.springframework.web.bind.annotation.ExceptionHandler(NonUniqueValueException.class)
    public ResponseEntity<Error> handleNonUniqueValueException(NonUniqueValueException exception) {
        ValidationErrorDetail validationError = exception.getValidationError();
        return buildResponseEntity(HttpStatus.CONFLICT, exception, validationError);
    }

    // Handles exceptions related to invalid tokens
    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Error> handleInvalidTokenException(RuntimeException exception) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, exception);
    }

    // Handles exceptions related to failed user login
    @org.springframework.web.bind.annotation.ExceptionHandler(LoginFailureException.class)
    public ResponseEntity<Error> handleLoginFailureException(RuntimeException exception) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, exception);
    }
}