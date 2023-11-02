package code.me.dropfolder.exception.type;

public class NonUniqueUsernameException extends RuntimeException {
    public NonUniqueUsernameException(String message) {
        super(message);
    }
}
