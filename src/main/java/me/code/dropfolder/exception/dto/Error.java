package me.code.dropfolder.exception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class Error {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    @JsonProperty("error")
    private Boolean error;

    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("message")
    private String message;

    public Error(HttpStatus status, Throwable ex) {
        this.timestamp = LocalDateTime.now();
        this.error = true;
        this.status = status;
        this.message = ex.getMessage();
    }

    public ResponseEntity<Error> toResponseEntity() {
        return ResponseEntity.status(this.status).body(this);
    }
}