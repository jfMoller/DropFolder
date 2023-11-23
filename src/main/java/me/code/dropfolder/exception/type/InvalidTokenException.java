package me.code.dropfolder.exception.type;

/**
 * Exception class representing the failure of token validation.
 * This exception is thrown when an invalid token is detected.
 */
public class InvalidTokenException extends RuntimeException {

    /**
     * Constructs an InvalidTokenException with the specified detail message.
     *
     * @param message the detail message.
     */
    public InvalidTokenException(String message) {
        super(message);
    }
}
