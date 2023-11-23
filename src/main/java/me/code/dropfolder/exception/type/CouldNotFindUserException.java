package me.code.dropfolder.exception.type;

/**
 * Exception class representing a scenario where a requested user could not be found.
 */
public class CouldNotFindUserException extends RuntimeException {

    /**
     * Constructs a CouldNotFindUserException with the specified detail message.
     *
     * @param message the detail message.
     */
    public CouldNotFindUserException(String message) {
        super(message);
    }
}
