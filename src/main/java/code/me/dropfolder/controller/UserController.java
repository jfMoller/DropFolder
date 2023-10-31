package code.me.dropfolder.controller;

import code.me.dropfolder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    private record UserCredentials(String email, String password) {
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUp(@RequestBody UserCredentials newUser) {
        return userService.signUp(newUser.email(), newUser.password());
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserCredentials user) {
        return userService.login(user.email(), user.password());
    }

}