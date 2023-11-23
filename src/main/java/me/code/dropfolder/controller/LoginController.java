package me.code.dropfolder.controller;

import me.code.dropfolder.dto.AuthSuccessDto;
import me.code.dropfolder.dto.SuccessDto;
import me.code.dropfolder.dto.UserCredentialsDto;
import me.code.dropfolder.exception.type.AuthenticationFailureException;
import me.code.dropfolder.exception.type.InvalidCredentialsException;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for handling user login.
 * Provides endpoints under the "/api/" path.
 */
@RestController
@RequestMapping("/api")
public class LoginController {

    private final AuthenticationProvider authenticationProvider;
    private final UserService userService;
    private final LoginValidator loginValidator;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * Constructor for the LoginController class.
     *
     * @param authenticationProvider The authentication provider used for user authentication.
     * @param loginValidator         The validator ensuring the correctness of login credentials.
     * @param userService            The service managing user-related operations.
     * @param jwtTokenUtil           The utility class handling JSON Web Tokens (JWTs) related to user authentication.
     */
    @Autowired
    public LoginController(
            AuthenticationProvider authenticationProvider,
            LoginValidator loginValidator,
            UserService userService,
            JwtTokenUtil jwtTokenUtil) {
        this.authenticationProvider = authenticationProvider;
        this.loginValidator = loginValidator;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * Handles user login requests, authenticates the user, and generates a JWT upon successful authentication.
     * Note that all exceptions in the method are handled by GlobalExceptionHandler.
     *
     * @param dto The UserCredentialsDto containing the user's login credentials.
     * @return A ResponseEntity containing the AuthSuccess dto.
     * @throws LoginFailureException if there is a failure in the login process.
     */
    @PostMapping("/login")
    public ResponseEntity<SuccessDto> login(@RequestBody UserCredentialsDto dto) {
        try {
            authenticateUser(dto);
            String token = generateTokenForUser(dto);
            return new AuthSuccessDto(HttpStatus.OK, "Login successful", token).toResponseEntity();

        } catch (Exception exception) {
            validateCredentials(dto);
            throw new LoginFailureException("Unable to login: " + exception.getMessage());
        }
    }

    /**
     * Authenticates the user using the provided credentials.
     *
     * @param dto The UserCredentialsDto containing the user's login credentials.
     */
    private void authenticateUser(UserCredentialsDto dto) {
        Authentication token = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        Authentication result = authenticationProvider.authenticate(token);

        if (isNotAuthenticated(result)) {
            throw new AuthenticationFailureException("Could not authenticate login request");
        }
    }

    /**
     * Checks if the user authentication result indicates a failure.
     *
     * @param result The authentication result.
     * @return {@code true} if the user is not authenticated, {@code false} otherwise.
     */
    private boolean isNotAuthenticated(Authentication result) {
        return !result.isAuthenticated();
    }

    /**
     * Generates a JWT for the authenticated user.
     *
     * @param dto The UserCredentialsDto containing the user's login credentials.
     * @return The generated JWT.
     */
    private String generateTokenForUser(UserCredentialsDto dto) {
        User user = userService.loadUserByUsername(dto.username());
        return jwtTokenUtil.generateToken(user);
    }

    /**
     * Validates the correctness of user login credentials, informs the user if their username or password is invalid.
     *
     * @param dto The UserCredentialsDto containing the user's login credentials.
     * @throws InvalidCredentialsException if the provided credentials are invalid.
     */
    private void validateCredentials(UserCredentialsDto dto) {
        loginValidator.validateUserCredentials(dto);
    }

}