package me.code.dropfolder.exceptions.types;

/**
 * Exception class representing the failure of token validation.
 * This exceptions is thrown when an invalid token is detected.
 */
public class InvalidTokenException extends RuntimeException {

    /**
     * Constructs an InvalidTokenException with the specified details message.
     *
     * @param message the details message.
     */
    public InvalidTokenException(String message) {
        super(message);
    }
}
