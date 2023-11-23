package me.code.dropfolder.exception.type;

import me.code.dropfolder.exception.dto.detail.FileOperationErrorDetail;

/**
 * Exception class representing a failure in downloading a file.
 * This exception is thrown when there is an issue during the file download operation and includes details about the error.
 */
public class FileDownloadFailureException extends FileOperationException {

    /**
     * Constructs a FileDownloadFailureException with the specified detail message and download error detail.
     *
     * @param message       the detail message.
     * @param downloadError the detailed information about the error during file download.
     */
    public FileDownloadFailureException(String message, FileOperationErrorDetail downloadError) {
        super(message, downloadError);
    }
}
