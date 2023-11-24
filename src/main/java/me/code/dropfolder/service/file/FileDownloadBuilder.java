package me.code.dropfolder.service.file;

import me.code.dropfolder.model.File;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * This class builds a Spring ResponseEntity for downloading a file.
 */
public class FileDownloadBuilder {

    private final File file;

    /**
     * Constructs a FileDownloadBuilder with the specified File.
     *
     * @param file The file to be downloaded.
     */
    public FileDownloadBuilder(File file) {
        this.file = file;
    }

    /**
     * Builds a ResponseEntity containing the file for download.
     *
     * @return ResponseEntity containing the file.
     */
    public ResponseEntity<ByteArrayResource> buildResponseEntity() {
        return ResponseEntity.ok()
                .headers(generateHeaders())
                .contentType(generateMediaType())
                .body(generateResource());
    }

    /**
     * Generates HttpHeaders for the file download, including content disposition and length.
     *
     * @return HttpHeaders for the file download.
     */
    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", file.getName());
        headers.setContentLength(file.getSize());

        return headers;
    }

    /**
     * Generates the MediaType based on the file's content type.
     *
     * @return MediaType for the file.
     */
    private MediaType generateMediaType() {
        return (file.getContentType() != null)
                ? MediaType.parseMediaType(file.getContentType())
                : MediaType.APPLICATION_OCTET_STREAM;
    }

    /**
     * Generates a ByteArrayResource for the file data.
     *
     * @return ByteArrayResource for the file data.
     */
    private ByteArrayResource generateResource() {
        return new ByteArrayResource(file.getData());
    }

}