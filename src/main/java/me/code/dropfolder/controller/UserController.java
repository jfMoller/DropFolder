package me.code.dropfolder.controller;

import me.code.dropfolder.dto.Success;
import me.code.dropfolder.dto.UserCredentials;
import me.code.dropfolder.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Success> signUp(@RequestBody UserCredentials newUser) {
        return userService.registerUser(newUser.username(), newUser.password());
    }

    @PostMapping("/login")
    public ResponseEntity<Success> login(@RequestBody UserCredentials user) {
        return userService.login(user.username(), user.password());
    }

}