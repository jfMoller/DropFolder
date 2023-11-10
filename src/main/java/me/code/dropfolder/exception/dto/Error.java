package me.code.dropfolder.exception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Error {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    @JsonProperty("error")
    private Boolean error;

    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("message")
    private String message;

    private List<ErrorDetail> errorDetails;

    @JsonProperty("errorDetails")
    private List<ErrorDetail> errorDetails() {
        return (errorDetails.size() > 0) ? errorDetails : null;
    }

    public Error(HttpStatus status, Throwable ex) {
        this.timestamp = LocalDateTime.now();
        this.error = true;
        this.status = status;
        this.message = ex.getMessage();
        this.errorDetails = new ArrayList<>();
    }

    public <T extends ErrorDetail> void addErrorDetail(T errorDetail) {
        errorDetails.add(errorDetail);
    }

    public ResponseEntity<Error> toResponseEntity() {
        return ResponseEntity.status(this.status).body(this);
    }
}