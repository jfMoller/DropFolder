package me.code.dropfolder.exception.type;

public class NonUniqueUsernameException extends RuntimeException {
    public NonUniqueUsernameException(String message) {
        super(message);
    }
}
