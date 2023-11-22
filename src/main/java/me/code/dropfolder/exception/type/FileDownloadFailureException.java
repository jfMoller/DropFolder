package me.code.dropfolder.exception.type;

public class FileDownloadFailureException extends RuntimeException {
    public FileDownloadFailureException(String message) {
        super(message);
    }
}
