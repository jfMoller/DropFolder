package me.code.dropfolder.exception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import me.code.dropfolder.exception.dto.detail.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object (DTO) representing an error response.
 */
@Getter
public class ErrorDto {

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

    /**
     * Constructor for creating an ErrorDto instance.
     *
     * @param status The HTTP status associated with the error.
     * @param ex     The throwable object representing the error.
     */
    public ErrorDto(HttpStatus status, Throwable ex) {
        this.timestamp = LocalDateTime.now();
        this.error = true;
        this.status = status;
        this.message = ex.getMessage();
        this.errorDetails = new ArrayList<>();
    }

    /**
     * Adds an error detail to the list of error details.
     *
     * @param errorDetail The error detail to be added.
     * @param <T>         A subtype of ErrorDetail.
     */
    public <T extends ErrorDetail> void addErrorDetail(T errorDetail) {
        errorDetails.add(errorDetail);
    }

    /**
     * Converts the ErrorDto to a ResponseEntity for sending as an API response.
     *
     * @return A ResponseEntity containing the ErrorDto.
     */
    public ResponseEntity<ErrorDto> toResponseEntity() {
        return ResponseEntity.status(this.status).body(this);
    }
}