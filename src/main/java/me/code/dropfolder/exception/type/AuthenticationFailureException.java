package me.code.dropfolder.exception.type;

/**
 * Exception class representing a failure during user authentication.
 */
public class AuthenticationFailureException extends RuntimeException {

    /**
     * Constructs an AuthenticationFailureException with the specified detail message.
     *
     * @param message the detail message.
     */
    public AuthenticationFailureException(String message) {
        super(message);
    }
}
