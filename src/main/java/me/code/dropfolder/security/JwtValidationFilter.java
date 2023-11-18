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
            FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        // Allow requests to login and signup endpoints without a valid token
        if (isPermittedEndpoint(request)) {
            filterChain.doFilter(request, response);
        } else if (jwtTokenProvider.isValidToken(token)) {
            String username = jwtTokenProvider.getTokenUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            var auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword());
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } else {
            throw new InvalidTokenException("The provided token is not valid.");
        }
    }

    private boolean isPermittedEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/user/login") || path.equals("/api/user/sign-up");
    }
}
