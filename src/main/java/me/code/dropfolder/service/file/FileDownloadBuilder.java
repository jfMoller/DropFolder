package me.code.dropfolder.service.file;

import me.code.dropfolder.model.File;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class FileDownloadBuilder {

    private final File file;

    public FileDownloadBuilder(File file) {
        this.file = file;
    }

    public ResponseEntity<ByteArrayResource> buildResponseEntity() {
        return ResponseEntity.ok()
                .headers(generateHeaders())
                .contentType(generateMediaType())
                .body(generateResource());
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", file.getName());
        headers.setContentLength(file.getSize());

        return headers;
    }

    // Determine the media type based on file content type
    private MediaType generateMediaType() {
        return (file.getContentType() != null)
                ? MediaType.parseMediaType(file.getContentType())
                : MediaType.APPLICATION_OCTET_STREAM;
    }

    private ByteArrayResource generateResource() {
        return new ByteArrayResource(file.getData());
    }

}