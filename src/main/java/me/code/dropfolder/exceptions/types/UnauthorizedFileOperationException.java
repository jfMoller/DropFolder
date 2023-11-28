package me.code.dropfolder.exceptions.types;

/**
 * Exception indicating an unauthorized file operation.
 *
 * <p>Thrown when a user lacks the necessary permissions for a file operation.
 */
public class UnauthorizedFileOperationException extends RuntimeException {

    /**
     * Constructs a new UnauthorizedFileOperationException with a specified details message.
     *
     * @param message Detail message describing the unauthorized file operation exceptions.
     */
    public UnauthorizedFileOperationException(String message) {
        super(message);
    }
}
