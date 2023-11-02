package code.me.dropfolder.service;

import code.me.dropfolder.exception.type.NonUniqueUsernameException;
import code.me.dropfolder.exception.type.PasswordFormattingException;
import code.me.dropfolder.exception.type.UsernameFormattingException;
import code.me.dropfolder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationValidator {

    private final UserRepository userRepository;

    @Autowired
    public UserRegistrationValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void findFormattingErrors(String username, String password) {
        if (hasUsernameFormattingError(username)) {
            throw new UsernameFormattingException(
                    "A username needs to be at least 3 characters long, and must not include non-alphanumeric characters");
        }
        if (hasPasswordFormattingError(password)) {
            throw new PasswordFormattingException(
                    "A password needs to be at least 6 characters long, and must include at least one uppercase letter");
        }
    }

    public boolean hasUsernameFormattingError(String username) {
        return username.isBlank() ||
                hasNonAlphanumericCharacters(username) ||
                isBelowMinLength(username, 3);
    }

    public boolean hasPasswordFormattingError(String password) {
        return password.isBlank() ||
                !containsUpperCase(password) ||
                isBelowMinLength(password, 6);
    }

    public boolean hasNonAlphanumericCharacters(String string) {
        // Regex to match non-alphanumeric characters
        String nonAlphanumericRegex = ".*[^a-zA-Z0-9].*";
        return string.matches(nonAlphanumericRegex);
    }

    private boolean isBelowMinLength(String string, int minLength) {
        return string.length() < minLength;
    }

    private boolean containsUpperCase(String string) {
        // Regex to match at least one uppercase letter
        String uppercaseRegex = ".*[A-Z].*";
        return string.matches(uppercaseRegex);
    }

    public void findNonUniqueUsername(String username) {
        if (isUsernameTaken(username)) {
            throw new NonUniqueUsernameException("An account with the chosen username already exists");
        }
    }

    private boolean isUsernameTaken(String username) {
        return userRepository.isPreexistingUsername(username);
    }
}
