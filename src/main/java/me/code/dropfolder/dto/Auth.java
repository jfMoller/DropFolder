package me.code.dropfolder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

public class Auth extends Success {

    @JsonProperty("token")
    private String token;

    public Auth(HttpStatus status, String message, String token) {
        super(status, message);
        this.token = token;
    }
}
