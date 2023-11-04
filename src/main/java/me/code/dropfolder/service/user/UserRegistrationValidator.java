package me.code.dropfolder.service.user;

import me.code.dropfolder.exception.dto.ValidationError;
import me.code.dropfolder.exception.type.NonUniqueUsernameException;
import me.code.dropfolder.exception.type.PasswordFormattingException;
import me.code.dropfolder.exception.type.UsernameFormattingException;
import me.code.dropfolder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationValidator {

    private final static int USERNAME_MIN_LENGTH = 3;
    private final static int USERNAME_MAX_LENGTH = 14;
    private final static int PASSWORD_MIN_LENGTH = 6;
    private final static int PASSWORD_MAX_LENGTH = 17;

    private final UserRepository userRepository;

    @Autowired
    public UserRegistrationValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void findFormattingErrors(String username, String password) {
        if (hasUsernameFormattingError(username)) {
            throw new UsernameFormattingException(
                    String.format("The username must be between %s-%s characters long, and must not include non-alphanumeric characters",
                            USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH));
        }
        if (hasPasswordFormattingError(password)) {
            throw new PasswordFormattingException(
                    String.format("The password must be at between %s-%s characters long, and must include at least one uppercase letter",
                            PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH));
        }
    }

    public boolean hasUsernameFormattingError(String username) {
        return username.isBlank() ||
                hasNonAlphanumericCharacters(username) ||
                isInvalidLength(username, USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH);
    }

    public boolean hasPasswordFormattingError(String password) {
        return password.isBlank() ||
                !containsUpperCase(password) ||
                isInvalidLength(password, PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH);
    }

    public boolean hasNonAlphanumericCharacters(String string) {
        // Regex to match non-alphanumeric characters
        String nonAlphanumericRegex = ".*[^a-zA-Z0-9].*";
        return string.matches(nonAlphanumericRegex);
    }

    private boolean isInvalidLength(String string, int minLength, int maxLength) {
        int length = string.length();
        return length < minLength || length > maxLength;
    }

    private boolean containsUpperCase(String string) {
        // Regex to match at least one uppercase letter
        String uppercaseRegex = ".*[A-Z].*";
        return string.matches(uppercaseRegex);
    }

    public void findNonUniqueUsername(String username) {
        if (isUsernameTaken(username)) {
            throw new NonUniqueUsernameException(
                    "An account with the chosen username already exists",
                    new ValidationError(
                            "user",
                            "username",
                            username,
                            "'" + username + "'" + " is not a unique username."));
        }
    }

    private boolean isUsernameTaken(String username) {
        return userRepository.isPreexistingUsername(username);
    }
}
