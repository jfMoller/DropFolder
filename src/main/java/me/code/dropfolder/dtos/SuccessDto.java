package me.code.dropfolder.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import me.code.dropfolder.dtos.details.SuccessDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

/**
 * A Data Transfer Object (DTO) representing a successful response.
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

    @JsonProperty("successDetails")
    private SuccessDetail successDetails;

    /**
     * Constructs a new SuccessDto with the specified status, message, and success details.
     *
     * @param status         The HTTP status of the successful response.
     * @param message        A message providing additional information about the successful response.
     * @param successDetails Details about the success.
     */
    public SuccessDto(HttpStatus status, String message, SuccessDetail successDetails) {
        this.timestamp = LocalDateTime.now();
        this.success = true;
        this.status = status;
        this.message = message;
        this.successDetails = successDetails;
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