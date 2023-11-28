package me.code.dropfolder.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing a success response.
 */
@Getter
public class SuccessDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("message")
    private String message;

    /**
     * Constructor for the SuccessDto class.
     *
     * @param status  The HTTP status of the success response.
     * @param message The success message.
     */
    public SuccessDto(HttpStatus status, String message) {
        this.timestamp = LocalDateTime.now();
        this.success = true;
        this.status = status;
        this.message = message;
    }

    /**
     * Converts the SuccessDto to a ResponseEntity for use in the API responses.
     *
     * @return A ResponseEntity containing the SuccessDto.
     */
    public ResponseEntity<SuccessDto> toResponseEntity() {
        return ResponseEntity.status(this.status).body(this);
    }
}