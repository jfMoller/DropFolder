package me.code.dropfolder.exceptions;

import jakarta.servlet.ServletException;
import me.code.dropfolder.exceptions.dtos.ErrorDto;
import me.code.dropfolder.exceptions.dtos.details.*;
import me.code.dropfolder.exceptions.types.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

/**
 * Global exceptions handler for handling and providing standardized responses for various exceptions.
 * This class extends ResponseEntityExceptionHandler to provide centralized exceptions handling for all controllers.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Builds a ResponseEntity with an ErrorDto for generic exceptions.
     *
     * @param status    The HTTP status to be set in the response.
     * @param exception The thrown exceptions.
     * @return A ResponseEntity containing an ErrorDto.
     */
    private ResponseEntity<ErrorDto> buildResponseEntity(HttpStatus status, Throwable exception) {
        return new ErrorDto(status, exception).toResponseEntity();
    }

    /**
     * Builds a ResponseEntity with an ErrorDto and a specific ErrorDetail for exceptions.
     *
     * @param status      The HTTP status to be set in the response.
     * @param exception   The thrown exceptions.
     * @param errorDetail The specific error details.
     * @param <T>         Type parameter representing the types of error details.
     * @return A ResponseEntity containing an ErrorDto with specified error details.
     */
    private <T extends ErrorDetail> ResponseEntity<ErrorDto> buildResponseEntity(
            HttpStatus status, Throwable exception, T errorDetail) {
        ErrorDto errorDto = new ErrorDto(status, exception);
        errorDto.addErrorDetail(errorDetail);
        return errorDto.toResponseEntity();
    }

    /**
     * Handles generic exceptions and returns a ResponseEntity with an ErrorDto.
     *
     * @param exception The generic exceptions to be handled.
     * @return A ResponseEntity containing an ErrorDto with details about the exceptions.
     */
    @ExceptionHandler({
            Exception.class,
            IOException.class,
            ServletException.class})
    public ResponseEntity<ErrorDto> handleException(Exception exception) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    /**
     * Handles abstract exceptions and returns a ResponseEntity with an ErrorDto.
     *
     * @param exception The abstract exceptions to be handled.
     * @return A ResponseEntity containing an ErrorDto with details about the exceptions.
     */
    @ExceptionHandler({AccountRegistrationException.class})
    public ResponseEntity<ErrorDto> handleAbstractException(Exception exception) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, exception);
    }

    /**
     * Handles exceptions related to validation and returns a ResponseEntity with an ErrorDto.
     *
     * @param exception The validation exceptions to be handled.
     * @return A ResponseEntity containing an ErrorDto with details about the validation exceptions.
     */
    @ExceptionHandler({
            UsernameValidationException.class,
            PasswordValidationException.class,
            NonUniqueValueException.class,
            InvalidCredentialsException.class})
    public ResponseEntity<ErrorDto> handleValidationException(ValidationException exception) {
        ValidationErrorDetail validationError = exception.getValidationError();
        return buildResponseEntity(HttpStatus.CONFLICT, exception, validationError);
    }

    /**
     * Handles exceptions related to values or objects that could not be found and returns a ResponseEntity with an ErrorDto.
     *
     * @param exception The exceptions related to the unavailability of values or objects.
     * @return A ResponseEntity containing an ErrorDto with details about the exceptions.
     */
    @ExceptionHandler({
            CouldNotFindUserException.class,
            CouldNotFindFolderException.class,
            CouldNotFindFileException.class})
    public ResponseEntity<ErrorDto> handleNotFoundException(Exception exception) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, exception);
    }

    /**
     * Handles exceptions related to file operations and returns a ResponseEntity with an ErrorDto.
     *
     * @param exception The file operation exceptions to be handled.
     * @return A ResponseEntity containing an ErrorDto with details about the file operation exceptions.
     */
    @ExceptionHandler({
            FileDownloadFailureException.class,
            FileDeletionFailureException.class})
    public ResponseEntity<ErrorDto> handleFileOperationsFailureException(FileOperationException exception) {
        FileOperationErrorDetail operationError = exception.getFileOperationErrorDetail();
        return buildResponseEntity(HttpStatus.BAD_REQUEST, exception, operationError);
    }

    /**
     * Handles exceptions specific to file upload failures and returns a ResponseEntity with an ErrorDto.
     *
     * @param exception The file upload failure exceptions to be handled.
     * @return A ResponseEntity containing an ErrorDto with details about the file upload failure.
     */
    @ExceptionHandler({FileUploadFailureException.class})
    public ResponseEntity<ErrorDto> handleFileUploadFailureException(FileUploadFailureException exception) {
        FileUploadErrorDetail uploadError = exception.getUploadError();
        return buildResponseEntity(HttpStatus.BAD_REQUEST, exception, uploadError);
    }

    /**
     * Handles exceptions related to folder operations and returns a ResponseEntity with an ErrorDto.
     *
     * @param exception The folder operation exceptions to be handled.
     * @return A ResponseEntity containing an ErrorDto with details about the folder operation exceptions.
     */
    @ExceptionHandler({FolderCreationFailureException.class})
    public ResponseEntity<ErrorDto> handleFolderOperationsFailureException(FolderOperationException exception) {
        FolderOperationErrorDetail operationError = exception.getFolderOperationErrorDetail();
        return buildResponseEntity(HttpStatus.BAD_REQUEST, exception, operationError);
    }

    /**
     * Handles exceptions related to failed authentication and returns a ResponseEntity with an ErrorDto.
     *
     * @param exception The authentication-related exceptions to be handled.
     * @return A ResponseEntity containing an ErrorDto with details about the authentication-related exceptions.
     */
    @ExceptionHandler({
            InvalidTokenException.class,
            LoginFailureException.class,
            AuthenticationFailureException.class})
    public ResponseEntity<ErrorDto> handleFailedAuthenticationException(Exception exception) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, exception);
    }

    /**
     * Handles exceptions related to failed authorization and returns a ResponseEntity with an ErrorDto.
     *
     * @param exception The authorization-related exceptions to be handled.
     * @return A ResponseEntity containing an ErrorDto with details about the authorization-related exceptions.
     */
    @ExceptionHandler({UnauthorizedFileOperationException.class})
    public ResponseEntity<ErrorDto> handleFailedAuthorizationException(Exception exception) {
        return buildResponseEntity(HttpStatus.FORBIDDEN, exception);
    }

}