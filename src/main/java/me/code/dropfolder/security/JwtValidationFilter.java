package me.code.dropfolder.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.code.dropfolder.exception.type.InvalidTokenException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtValidationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JwtTokenProvider jwtTokenProvider;

    public JwtValidationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = new JwtTokenProvider();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain securityFilterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        boolean isMissingToken = (token == null || token.isBlank());

        if (isMissingToken) { // proceed without authentication
            securityFilterChain.doFilter(request, response);
        }

        boolean isValidToken = jwtTokenProvider.isValidToken(token);

        if (isValidToken) { // proceed with authentication
            setAuthenticationContext(token);
            securityFilterChain.doFilter(request, response);

        } else throw new InvalidTokenException("The provided token is not valid.");
    }

    private void setAuthenticationContext(String token) {
        UserDetails userDetails = getUserDetails(token);
        var authToken = getAuthToken(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private UserDetails getUserDetails(String token) {
        String username = jwtTokenProvider.getTokenUsername(token);
        return this.userDetailsService.loadUserByUsername(username);
    }

    private UsernamePasswordAuthenticationToken getAuthToken(UserDetails user) {
        return new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}
