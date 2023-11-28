package me.code.dropfolder.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

/**
 * Data Transfer Object (DTO) representing an authentication success response with a token.
 * Extends the SuccessDto class.
 */
public class AuthSuccessDto extends SuccessDto {

    @JsonProperty("token")
    private String token;

    /**
     * Constructor for the AuthSuccessDto class.
     *
     * @param status  The HTTP status of the success response.
     * @param message The success message.
     * @param token   The authentication (JWT) token.
     */
    public AuthSuccessDto(HttpStatus status, String message, String token) {
        super(status, message);
        this.token = token;
    }
}
