package me.code.dropfolder.controller;

import me.code.dropfolder.dto.Auth;
import me.code.dropfolder.dto.Success;
import me.code.dropfolder.dto.UserCredentials;
import me.code.dropfolder.exception.type.AuthenticationFailureException;
import me.code.dropfolder.exception.type.LoginFailureException;
import me.code.dropfolder.model.User;
import me.code.dropfolder.security.JwtTokenUtil;
import me.code.dropfolder.service.login.LoginValidator;
import me.code.dropfolder.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class LoginController {

    private final AuthenticationProvider authenticationProvider;

    private final UserService userService;

    private final LoginValidator loginValidator;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public LoginController(
            AuthenticationProvider authenticationProvider,
            LoginValidator loginValidator,
            UserService userDetailsService,
            JwtTokenUtil jwtTokenUtil) {
        this.authenticationProvider = authenticationProvider;
        this.loginValidator = loginValidator;
        this.userService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Success> login(@RequestBody UserCredentials dto)
            throws AuthenticationFailureException, LoginFailureException {
        try {
            if (isUserAuthenticated(dto.username(), dto.password())) {
                User user = userService.loadUserByUsername(dto.username());
                String token = jwtTokenUtil.generateToken(user);

                return new Auth(HttpStatus.OK, "Login successful", token).toResponseEntity();

            } else {
                throw new AuthenticationFailureException("Could not authenticate login request");
            }

        } catch (Exception exception) {
            // Throws specific exceptions if the username or password are invalid
            loginValidator.validateUserCredentials(dto);
            throw new LoginFailureException("Unable to login: " + exception.getMessage());
        }
    }

    private boolean isUserAuthenticated(String username, String password) {
        Authentication token = new UsernamePasswordAuthenticationToken(username, password);
        var result = authenticationProvider.authenticate(token);

        return result.isAuthenticated();
    }

}