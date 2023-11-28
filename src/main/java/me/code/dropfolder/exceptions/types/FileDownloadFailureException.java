package me.code.dropfolder.exceptions.types;

import me.code.dropfolder.exceptions.dtos.details.FileOperationErrorDetail;

/**
 * Exception class representing a failure in downloading a file.
 * This exceptions is thrown when there is an issue during the file download operation and includes details about the error.
 */
public class FileDownloadFailureException extends FileOperationException {

    /**
     * Constructs a FileDownloadFailureException with the specified details message and download error details.
     *
     * @param message       the details message.
     * @param downloadError the detailed information about the error during file download.
     */
    public FileDownloadFailureException(String message, FileOperationErrorDetail downloadError) {
        super(message, downloadError);
    }
}
