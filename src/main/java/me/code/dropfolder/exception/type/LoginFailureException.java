package me.code.dropfolder.exception.type;

/**
 * Exception class representing a failure in the login process.
 * This exception is thrown when a login attempt fails.
 */
public class LoginFailureException extends RuntimeException {

    /**
     * Constructs a LoginFailureException with the specified detail message.
     *
     * @param message the detail message.
     */
    public LoginFailureException(String message) {
        super(message);
    }
}
