package me.code.dropfolder.service.user;

import me.code.dropfolder.auth.JwtTokenProvider;
import me.code.dropfolder.dto.Auth;
import me.code.dropfolder.dto.Success;
import me.code.dropfolder.exception.type.AccountRegistrationException;
import me.code.dropfolder.exception.type.LoginFailureException;
import me.code.dropfolder.model.User;
import me.code.dropfolder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final UserRegistrationValidator userRegistrationValidator;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserRegistrationValidator userRegistrationValidator,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.userRegistrationValidator = userRegistrationValidator;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public ResponseEntity<Success> registerUser(String username, String password) {
        // Throws exceptions if there are formatting errors
        userRegistrationValidator.findFormattingErrors(username, password);

        // Throws an exception if the chosen username already exists
        userRegistrationValidator.findNonUniqueUsername(username);

        try {
            userRepository.save(new User(username, password));
            return new Success(
                    HttpStatus.CREATED,
                    "Successfully registered a new account with username: " + username)
                    .toResponseEntity();

        } catch (Exception exception) {
            throw new AccountRegistrationException("Failed to register a new account: " + exception.getMessage());
        }
    }

    public ResponseEntity<Success> login(String username, String password) {
        try {
            User user = userRepository.findUser(username, password);
            String token = jwtTokenProvider.generateToken(user);
            return new Auth(HttpStatus.OK, "Login successful", token).toResponseEntity();

        } catch (Exception exception) {
            throw new LoginFailureException("Login failed: " + exception.getMessage());
        }
    }

    public long getUserId(String username) {
        Optional<Long> id = userRepository.findUserId(username);
        if (id.isPresent()) {
            return id.get();
        } else return -1;
    }

    public void deleteUser(String username) {
        long id = getUserId(username);
        if (id != -1) {
            userRepository.deleteById(id);
        }
    }
}
