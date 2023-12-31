package me.code.dropfolder.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import me.code.dropfolder.exceptions.types.InvalidTokenException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A filter for validating and processing JWT (JSON Web Token) authentication in the servlet request.
 */
public class JwtValidationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * Constructs a new JwtValidationFilter with the provided JwtTokenUtil and UserDetailsService.
     *
     * @param jwtTokenUtil       The JwtTokenUtil instance for JWT-related operations.
     * @param userDetailsService The UserDetailsService implementation to retrieve user details for token validation.
     */
    public JwtValidationFilter(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Performs the filter logic to validate and process JWT authentication.
     *
     * @param request             The servlet request.
     * @param response            The servlet response.
     * @param securityFilterChain The filter chain for security.
     * @throws ServletException      If an error occurs during servlet processing.
     * @throws IOException           If an I/O exceptions occurs.
     * @throws InvalidTokenException If the provided token is not valid
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain securityFilterChain)
            throws ServletException, IOException {

        String token = request.getHeader(AUTHORIZATION_HEADER);

        if (isTokenMissing(token)) {
            continueFilterChain(securityFilterChain, request, response);

        } else if (isValidToken(token)) {
            continueFilterChainWithAuthentication(token, securityFilterChain, request, response);

        } else {
            throw new InvalidTokenException("The provided token is not valid.");
        }
    }

    /**
     * Checks if the provided JWT token is missing or blank.
     *
     * @param token The JWT token to be checked.
     * @return true if the token is null or blank, false otherwise.
     */
    private boolean isTokenMissing(String token) {
        return token == null || token.isBlank();
    }

    /**
     * Validates if the provided JWT token is valid using JwtTokenUtil.
     *
     * @param token The JWT token to be validated.
     * @return true if the token is valid, false otherwise.
     */
    private boolean isValidToken(String token) {
        return jwtTokenUtil.isValidToken(token);
    }

    /**
     * Continues the filter chain by invoking the next filter in the chain.
     *
     * @param filterChain The filter chain to be continued.
     * @param request     The servlet request.
     * @param response    The servlet response.
     * @throws ServletException If an exceptions occurs during servlet processing.
     * @throws IOException      If an I/O exceptions occurs.
     */
    private void continueFilterChain(
            FilterChain filterChain,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handleFilterChainException(e);
        }
    }

    /**
     * Sets the authentication context based on the provided JWT token.
     *
     * @param token The JWT token from which to extract user details.
     */
    private void setAuthenticationContext(String token) {
        UserDetails userDetails = getUserDetails(token);
        var authToken = getAuthToken(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    /**
     * Continues the filter chain after setting the authentication context.
     *
     * @param token       The JWT token.
     * @param filterChain The filter chain to be continued.
     * @param request     The servlet request.
     * @param response    The servlet response.
     * @throws ServletException If an exceptions occurs during servlet processing.
     * @throws IOException      If an I/O exceptions occurs.
     */
    private void continueFilterChainWithAuthentication(
            String token,
            FilterChain filterChain,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        setAuthenticationContext(token);
        continueFilterChain(filterChain, request, response);
    }

    /**
     * Retrieves UserDetails based on the username extracted from the JWT token.
     *
     * @param token The JWT token from which to extract the username.
     * @return UserDetails object for the specified username.
     */
    private UserDetails getUserDetails(String token) {
        String username = jwtTokenUtil.getTokenUsername(token);
        return this.userDetailsService.loadUserByUsername(username);
    }

    /**
     * Creates an authentication token based on UserDetails.
     *
     * @param user The UserDetails object.
     * @return An authentication token.
     */
    private UsernamePasswordAuthenticationToken getAuthToken(UserDetails user) {
        return new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    /**
     * Handles exceptions that may occur during the filter chain processing.
     *
     * @param exception The exceptions to be handled.
     * @throws ServletException If the exceptions is a ServletException.
     * @throws IOException      If the exceptions is an IOException.
     */
    private void handleFilterChainException(Exception exception) throws ServletException, IOException {
        if (exception instanceof ServletException) {
            throw new ServletException("Servlet exceptions: " + exception.getMessage());
        } else if (exception instanceof IOException) {
            throw new IOException("IO exceptions: " + exception.getMessage());
        }
    }
}
