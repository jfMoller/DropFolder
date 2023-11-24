package me.code.dropfolder.exception.type;

/**
 * Exception indicating an unauthorized file operation.
 *
 * <p>Thrown when a user lacks the necessary permissions for a file operation.
 */
public class UnauthorizedFileOperationException extends RuntimeException {

    /**
     * Constructs a new UnauthorizedFileOperationException with a specified detail message.
     *
     * @param message Detail message describing the unauthorized file operation exception.
     */
    public UnauthorizedFileOperationException(String message) {
        super(message);
    }
}
