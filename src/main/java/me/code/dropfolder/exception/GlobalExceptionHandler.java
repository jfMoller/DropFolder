package me.code.dropfolder.exception;

import me.code.dropfolder.exception.dto.ErrorDto;
import me.code.dropfolder.exception.dto.detail.ErrorDetail;
import me.code.dropfolder.exception.dto.detail.UploadErrorDetail;
import me.code.dropfolder.exception.dto.detail.ValidationErrorDetail;
import me.code.dropfolder.exception.type.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.rmi.ServerException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<ErrorDto> buildResponseEntity(HttpStatus status, Throwable exception) {
        return new ErrorDto(status, exception).toResponseEntity();
    }

    private <T extends ErrorDetail> ResponseEntity<ErrorDto> buildResponseEntity(HttpStatus status, Throwable exception, T errorDetail) {
        ErrorDto errorDto = new ErrorDto(status, exception);
        errorDto.addErrorDetail(errorDetail);
        return errorDto.toResponseEntity();
    }

    // Handles generic exceptions
    @ExceptionHandler({Exception.class, IOException.class, ServerException.class, AccountRegistrationException.class})
    public ResponseEntity<ErrorDto> handleException(Exception exception) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    // Handles exceptions related to validation exceptions
    @ExceptionHandler({UsernameValidationException.class, PasswordValidationException.class, NonUniqueValueException.class, InvalidCredentialsException.class})
    public ResponseEntity<ErrorDto> handleFormattingException(ValidationException exception) {
        ValidationErrorDetail validationError = exception.getValidationError();
        return buildResponseEntity(HttpStatus.CONFLICT, exception, validationError);
    }

    // Handles exceptions related to values or object that could not be found
    @ExceptionHandler({CouldNotFindUserException.class, CouldNotFindFolderException.class, CouldNotFindFileException.class})
    public ResponseEntity<ErrorDto> handleNotFoundException(Exception exception) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, exception);
    }

    // Handles exceptions related to file uploads
    @ExceptionHandler(FileUploadFailureException.class)
    public ResponseEntity<ErrorDto> handleFileUploadFailureException(FileUploadFailureException exception) {
        UploadErrorDetail uploadError = exception.getUploadError();
        return buildResponseEntity(HttpStatus.BAD_REQUEST, exception, uploadError);
    }

    // Handles exceptions related to failed authentication
    @ExceptionHandler({InvalidTokenException.class, LoginFailureException.class, AuthenticationFailureException.class})
    public ResponseEntity<ErrorDto> handleInvalidTokenException(Exception exception) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, exception);
    }

}