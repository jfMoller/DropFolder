package me.code.dropfolder.exception.type;

public class FileDeletionFailureException extends RuntimeException {
    public FileDeletionFailureException(String message) {
        super(message);
    }
}
