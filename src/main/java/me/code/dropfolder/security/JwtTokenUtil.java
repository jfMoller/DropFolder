package me.code.dropfolder.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import me.code.dropfolder.model.User;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.security.Key;
import java.util.Map;

/**
 * Utility class for handling JWT (JSON Web Token) operations, such as token generation, validation, and retrieval of claims.
 */
@Component
@NoArgsConstructor
public class JwtTokenUtil {

    private static final String CONFIG_FILE = "secrets-config.yml";
    private static final String JWT_SECRET = "jwt-secret";

    /**
     * Secret key used for JWT signing and validation.
     */
    private final String secret = getJwtSecret();

    /**
     * The cryptographic key derived from the secret for JWT operations.
     */
    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());

    /**
     * Generates a JWT based on the provided user information.
     *
     * @param user The user for whom the token is generated.
     * @return A JWT string.
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
     * Validates the given JWT.
     *
     * @param token The JWT to be validated.
     * @return true if the token is valid, false otherwise.
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

    /**
     * Retrieves the username from the given JWT.
     *
     * @param token The JWT from which to extract the username.
     * @return The username contained in the token.
     */
    public String getTokenUsername(String token) {
        return getTokenClaim(token, "username", String.class);
    }

    /**
     * Retrieves the user ID from the given JWT.
     *
     * @param token The JWT from which to extract the user ID.
     * @return The user ID contained in the token.
     */
    public long getTokenUserId(String token) {
        String userIdString = getTokenClaim(token, "id", String.class);
        return Long.parseLong(userIdString);
    }

    /**
     * Retrieves a specific claim from the given JWT.
     *
     * @param token      The JWT from which to extract the claim.
     * @param type       The type of claim to retrieve.
     * @param returnType The class type of the expected return value.
     * @param <T>        The generic type of the return value.
     * @return The claim value of the specified type.
     */
    public <T> T getTokenClaim(String token, String type, Class<T> returnType) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(type, returnType);
    }

    /**
     * Retrieves the JWT secret key from the configuration file.
     * <p>
     * This method reads the configuration file, specified by the constant {@code CONFIG_FILE},
     * and extracts the JWT secret key from it.
     * <p>
     * The configuration file is expected to be in YAML format, and the key for the JWT secret
     * is defined by the constant {@code JWT_SECRET}.
     *
     * @return The JWT secret key as a {@code String} if found in the configuration file,
     * or {@code null} if the key is not present or an error occurs while reading the file.
     * @see JwtTokenUtil#CONFIG_FILE
     * @see JwtTokenUtil#JWT_SECRET
     */
    private String getJwtSecret() {
        InputStream inputStream = JwtTokenUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
        Map<String, String> config = new Yaml().load(inputStream);

        return config.get(JWT_SECRET);
    }
}
