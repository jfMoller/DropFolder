package me.code.dropfolder.exception.type;

/**
 * Exception class representing a scenario where a requested folder could not be found.
 */
public class CouldNotFindFolderException extends RuntimeException {

    /**
     * Constructs a CouldNotFindFolderException with the specified detail message.
     *
     * @param message the detail message.
     */
    public CouldNotFindFolderException(String message) {
        super(message);
    }
}
