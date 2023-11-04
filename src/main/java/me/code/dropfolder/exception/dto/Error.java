package me.code.dropfolder.exception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Error {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    @JsonProperty("error")
    private Boolean error;

    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("message")
    private String message;

    @JsonProperty("subErrors")
    private List<SubError> subErrors;


    public Error(HttpStatus status, Throwable ex) {
        this.timestamp = LocalDateTime.now();
        this.error = true;
        this.status = status;
        this.message = ex.getMessage();
        this.subErrors = new ArrayList<>();
    }

    public <T extends SubError> void addSubError(T error) {
        subErrors.add(error);
    }

    public ResponseEntity<Error> toResponseEntity() {
        return ResponseEntity.status(this.status).body(this);
    }
}