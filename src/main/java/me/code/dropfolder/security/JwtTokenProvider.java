package me.code.dropfolder.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import me.code.dropfolder.exception.type.InvalidTokenException;
import me.code.dropfolder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * Provides functionality for generating and validating JWT tokens.
 */
@Component
public class JwtTokenProvider {

    private final String secret = "keyboardcat-fwjfw732842ndeADEUfui39429824jdmedwiaei";

    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());

    @Autowired
    public JwtTokenProvider() {
    }

    /**
     * Generates a JWT token for the given user.
     *
     * @param user The user for whom the token will be generated.
     * @return The generated JWT token as a string.
     */
    public String generateToken(User user) {
        String id = Long.toString(user.getId());
        String username = user.getUsername();

        return Jwts.builder()
                .setSubject(id)
                .claim("id", id)
                .claim("username", username)
                .signWith(key)
                .compact();
    }

    /**
     * Validates a JWT token.
     *
     * @param token The token to be validated.
     * @return True if the token is valid, otherwise an InvalidTokenException is thrown.
     * @throws InvalidTokenException If the token is malformed or has an invalid signature.
     */
    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException jwtException) {
            return false;
        }
    }

    public String getTokenUsername(String token) {
        return getTokenClaim(token, "username", String.class);
    }

    public <T> T getTokenClaim(String token, String type, Class<T> returnType) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(type, returnType);
    }
}
