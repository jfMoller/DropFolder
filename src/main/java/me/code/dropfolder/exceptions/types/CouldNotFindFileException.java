package me.code.dropfolder.exceptions.types;

/**
 * Exception class representing a scenario where a requested file could not be found.
 */
public class CouldNotFindFileException extends RuntimeException {

    /**
     * Constructs a CouldNotFindFileException with the specified details message.
     *
     * @param message the details message.
     */
    public CouldNotFindFileException(String message) {
        super(message);
    }
}
