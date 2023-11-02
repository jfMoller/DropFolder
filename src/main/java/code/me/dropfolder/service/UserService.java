package code.me.dropfolder.service;

import code.me.dropfolder.dto.Success;
import code.me.dropfolder.exception.type.AccountRegistrationException;
import code.me.dropfolder.exception.type.RegistrationFormattingException;
import code.me.dropfolder.model.User;
import code.me.dropfolder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<Success> signUp(String username, String password) {
        if (hasFormattingErrors(username, password)) {
            throw new RegistrationFormattingException("Invalid formatting detected");
        }
        try {
            User newUser = new User(username, password);
            userRepository.save(newUser);
            boolean hasBeenRegistered = isExistingUser(newUser.getId());
            if (hasBeenRegistered) {
                return new Success(
                        HttpStatus.CREATED, "Account registration was successful for username: " + newUser.getUsername())
                        .toResponseEntity();
            }
        } catch (Exception exception) {
            throw new AccountRegistrationException("Failed to register a new account: " + exception.getMessage());
        }
        throw new AccountRegistrationException("Unable to register a new account");
    }

    public ResponseEntity<Success> login(String username, String password) {
        return null;
    }

    public boolean hasFormattingErrors(String username, String password) {
        return username.isBlank() ||
                hasNonAlphanumericCharacters(username) ||
                !hasMinLength(username, 3)
                ||
                password.isBlank() ||
                !containsUpperCase(password) ||
                !hasMinLength(password, 6);
    }

    public boolean hasNonAlphanumericCharacters(String string) {
        // Regular expression to match non-alphanumeric characters
        String nonAlphanumericRegex = ".*[^a-zA-Z0-9].*";
        return string.matches(nonAlphanumericRegex);
    }

    private boolean hasMinLength(String string, int minLength) {
        return string.length() >= minLength;
    }

    private boolean containsUpperCase(String string) {
        // Regex to match at least one uppercase letter
        String uppercaseRegex = ".*[A-Z].*";
        return string.matches(uppercaseRegex);
    }


    private boolean isExistingUser(long id) {
        Optional<User> user = userRepository.findById(id);
        return user.isPresent();
    }

    public long getUserId(String username) {
        Optional<Long> id = userRepository.findUserId(username);
        if (id.isPresent()) {
            return id.get();
        }

        return -1;
    }

    public void deleteUser(String username) {
        long id = getUserId(username);
        if (id != -1) {
            userRepository.deleteById(id);
        }
    }
}
