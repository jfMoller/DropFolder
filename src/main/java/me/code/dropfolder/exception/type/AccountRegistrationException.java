package me.code.dropfolder.exception.type;

/**
 * Exception class representing an error during the account registration process.
 */
public class AccountRegistrationException extends RuntimeException {

    /**
     * Constructs an AccountRegistrationException with the specified detail message.
     *
     * @param message the detail message.
     */
    public AccountRegistrationException(String message) {
        super(message);
    }
}
