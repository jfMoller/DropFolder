package me.code.dropfolder.controllers;

import me.code.dropfolder.dtos.SuccessDto;
import me.code.dropfolder.dtos.UserCredentialsDto;
import me.code.dropfolder.services.UserService;
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
     * @param userService The services for managing user-related business-logic.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles user registration requests, registers the user, and returns a success response.
     *
     * @param dto The UserCredentialsDto containing the user's registration credentials.
     * @return A ResponseEntity containing the Success dtos.
     */
    @PostMapping("/register")
    public ResponseEntity<SuccessDto> signUp(@RequestBody UserCredentialsDto dto) {
        SuccessDto result = userService.registerUser(dto.username(), dto.password());
        return result.toResponseEntity();
    }

}