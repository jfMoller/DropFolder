package me.code.dropfolder.controller;

import me.code.dropfolder.dto.Success;
import me.code.dropfolder.dto.UserCredentialsDto;
import me.code.dropfolder.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class responsible for handling user-related operations.
 * Provides endpoints under the "/api/user" path.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    /**
     * Constructor for the UserController class.
     *
     * @param userService The service for managing user-related business-logic.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles user registration requests, registers the user, and returns a success response.
     *
     * @param dto The UserCredentialsDto containing the user's registration credentials.
     * @return A ResponseEntity containing the Success dto.
     */
    @PostMapping("/register")
    public ResponseEntity<Success> signUp(@RequestBody UserCredentialsDto dto) {
        Success result = userService.registerUser(dto.username(), dto.password());
        return result.toResponseEntity();
    }

}