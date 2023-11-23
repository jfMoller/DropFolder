package me.code.dropfolder.exception.type;

/**
 * Exception class representing a scenario where a requested file could not be found.
 */
public class CouldNotFindFileException extends RuntimeException {

    /**
     * Constructs a CouldNotFindFileException with the specified detail message.
     *
     * @param message the detail message.
     */
    public CouldNotFindFileException(String message) {
        super(message);
    }
}
