package me.code.dropfolder.exceptions.types;

/**
 * Exception class representing a failure in the login process.
 * This exceptions is thrown when a login attempt fails.
 */
public class LoginFailureException extends RuntimeException {

    /**
     * Constructs a LoginFailureException with the specified details message.
     *
     * @param message the details message.
     */
    public LoginFailureException(String message) {
        super(message);
    }
}
