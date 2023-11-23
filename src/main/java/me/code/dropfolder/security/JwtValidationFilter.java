package me.code.dropfolder.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import me.code.dropfolder.exception.type.InvalidTokenException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtValidationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtValidationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = new JwtTokenUtil();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain securityFilterChain)
            throws ServletException, IOException, InvalidTokenException {

        String token = request.getHeader(AUTHORIZATION_HEADER);

        if (isTokenMissing(token)) {
            continueFilterChain(securityFilterChain, request, response);

        } else if (isValidToken(token)) {
            continueFilterChainWithAuthentication(token, securityFilterChain, request, response);

        } else {
            throw new InvalidTokenException("The provided token is not valid.");
        }
    }

    private boolean isTokenMissing(String token) {
        return token == null || token.isBlank();
    }

    private boolean isValidToken(String token) {
        return jwtTokenUtil.isValidToken(token);
    }

    private void continueFilterChain(
            FilterChain filterChain,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ServletException | IOException e) {
            handleFilterChainException(e);
        }
    }

    private void setAuthenticationContext(String token) {
        UserDetails userDetails = getUserDetails(token);
        var authToken = getAuthToken(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private void continueFilterChainWithAuthentication(
            String token,
            FilterChain filterChain,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        setAuthenticationContext(token);
        continueFilterChain(filterChain, request, response);
    }

    private UserDetails getUserDetails(String token) {
        String username = jwtTokenUtil.getTokenUsername(token);
        return this.userDetailsService.loadUserByUsername(username);
    }

    private UsernamePasswordAuthenticationToken getAuthToken(UserDetails user) {
        return new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    private void handleFilterChainException(Exception exception) throws ServletException, IOException {
        if (exception instanceof ServletException) {
            throw new ServletException("Servlet exception: " + exception.getMessage());
        } else if (exception instanceof IOException) {
            throw new IOException("IO exception: " + exception.getMessage());
        }
    }
}
