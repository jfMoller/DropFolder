package me.code.dropfolder.dtos.details;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A specialized SuccessDetail class representing successful authentication.
 */
public class AuthSuccessDetail extends SuccessDetail {

    /**
     * The authentication JWT token associated with the successful authentication.
     */
    @JsonProperty("token")
    private String token;

    /**
     * Constructs a new AuthSuccessDetail with the specified authentication token.
     *
     * @param token The authentication token associated with the successful authentication.
     */
    public AuthSuccessDetail(String token) {
        this.token = token;
    }
}
