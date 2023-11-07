package me.code.dropfolder.service.user;

import me.code.dropfolder.exception.dto.ValidationErrorDetail;
import me.code.dropfolder.exception.type.NonUniqueValueException;
import me.code.dropfolder.exception.type.PasswordFormattingException;
import me.code.dropfolder.exception.type.UsernameFormattingException;
import me.code.dropfolder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class validates user credentials and throws exceptions
 * if any formatting or uniqueness errors are found.
 */
@Component
public class UserRegistrationValidator {

    /**
     * The constants below are used to define various constraints and patterns for
     * validating user credentials, as well as for generating validation error details.
     */

    // Lower and upper bound for username length
    private final static int USERNAME_MIN_LENGTH = 3;
    private final static int USERNAME_MAX_LENGTH = 14;

    // Lower and upper bound for password length
    private final static int PASSWORD_MIN_LENGTH = 6;
    private final static int PASSWORD_MAX_LENGTH = 17;

    // Field names; used to assign the targeted field when generating validation error details
    private final static String USERNAME_FIELD = "username";
    private final static String PASSWORD_FIELD = "password";

    // Regex patterns; used for format validation of user credentials
    private final static String NON_ALPHANUMERIC_REGEX = ".*[^a-zA-Z0-9].*";
    private final static String UPPERCASE_REGEX = ".*[A-Z].*";

    // Exception error messages; determines the primary message passed as part of exceptions (currently username and password related exceptions)
    private final static String INVALID_USERNAME_ERROR_MESSAGE =
            String.format("The username must be between %s-%s characters long, and must not include non-alphanumeric characters",
                    USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH);

    private final static String INVALID_PASSWORD_ERROR_MESSAGE =
            String.format("The password must be between %s-%s characters long, and must include at least one uppercase letter",
                    PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH);

    // Error detail messages; determines the message passed as part of the ValidationErrorDetail
    private final static String INVALID_USERNAME_LENGTH_ERROR_MESSAGE =
            String.format("Must be between %s-%s characters long", USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH);

    private final static String INVALID_PASSWORD_LENGTH_ERROR_MESSAGE =
            String.format("Must be between %s-%s characters long", PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH);

    private final static String NON_ALPHANUMERIC_ERROR_MESSAGE = "Contains non-alphanumeric characters";

    private final static String NO_UPPERCASE_ERROR_MESSAGE = "Does not contain any uppercase characters";

    private final static String NULL_FIELD_ERROR_MESSAGE = "The chosen value is null";

    private final static String BLANK_FIELD_ERROR_MESSAGE = "The chosen value is blank";

    private final static String UNKNOWN_FIELD_ERROR_MESSAGE = "The chosen value is blank";

    private final static String NON_UNIQUE_USERNAME_ERROR_MESSAGE = "Is not a unique username";

    private final UserRepository userRepository;

    @Autowired
    public UserRegistrationValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds formatting errors in the provided username and password and throws exceptions if any are found.
     *
     * @param username The username to be validated.
     * @param password The password to be validated.
     * @throws UsernameFormattingException If there are formatting errors in the username.
     * @throws PasswordFormattingException If there are formatting errors in the password.
     */
    public void findFormattingErrors(String username, String password) {
        findNullValues(username, password);

        if (hasUsernameFormattingError(username)) {
            throw new UsernameFormattingException(
                    INVALID_USERNAME_ERROR_MESSAGE,
                    generateValidationErrorDetail(USERNAME_FIELD, username));

        }
        if (hasPasswordFormattingError(password)) {
            throw new PasswordFormattingException(
                    INVALID_PASSWORD_ERROR_MESSAGE,
                    generateValidationErrorDetail(PASSWORD_FIELD, password));
        }
    }

    /**
     * Ensures that the provided username and password are not null.
     * If either of them is null, it throws a respective formatting exception.
     *
     * @param username The username to be validated.
     * @param password The password to be validated.
     * @throws UsernameFormattingException If the username is null.
     * @throws PasswordFormattingException If the password is null.
     */
    public void findNullValues(String username, String password) {
        if (username == null) {
            throw new UsernameFormattingException(
                    INVALID_USERNAME_ERROR_MESSAGE,
                    generateValidationErrorDetail(USERNAME_FIELD, null));

        }

        if (password == null) {
            throw new PasswordFormattingException(
                    INVALID_PASSWORD_ERROR_MESSAGE,
                    generateValidationErrorDetail(PASSWORD_FIELD, null));

        }
    }

    /**
     * Checks if there are formatting errors in the provided username.
     *
     * @param username The username to be validated.
     * @return True if there are formatting errors, otherwise false.
     */
    public boolean hasUsernameFormattingError(String username) {
        if (username == null) return true;
        else return username.isBlank() ||
                hasNonAlphanumericCharacters(username) ||
                isInvalidUsernameLength(username);
    }

    /**
     * Checks if there are formatting errors in the provided password.
     *
     * @param password The password to be validated.
     * @return True if there are formatting errors, otherwise false.
     */
    public boolean hasPasswordFormattingError(String password) {
        if (password == null) return true;
        else return password.isBlank() ||
                doesNotContainUppercase(password) ||
                isInvalidPasswordLength(password);
    }

    /**
     * Checks if the provided string contains non-alphanumeric characters.
     *
     * @param string The string to be checked.
     * @return True if there are non-alphanumeric characters, otherwise false.
     */
    public boolean hasNonAlphanumericCharacters(String string) {
        return string.matches(NON_ALPHANUMERIC_REGEX);
    }

    /**
     * Checks if the length of the provided string is within the specified range.
     *
     * @param string    The string to be checked.
     * @param minLength The minimum allowed length.
     * @param maxLength The maximum allowed length.
     * @return True if the length is invalid, otherwise false.
     */
    private boolean isInvalidLength(String string, int minLength, int maxLength) {
        int length = string.length();
        return length < minLength || length > maxLength;
    }

    /**
     * Checks if the length of the provided username is within the specified range.
     *
     * @param username The username to be checked.
     * @return True if the length is invalid, otherwise false.
     */
    private boolean isInvalidUsernameLength(String username) {
        return isInvalidLength(username, USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH);
    }

    /**
     * Checks if the length of the provided password is within the specified range.
     *
     * @param password The password to be checked.
     * @return True if the length is invalid, otherwise false.
     */
    private boolean isInvalidPasswordLength(String password) {
        return isInvalidLength(password, PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH);
    }

    /**
     * Checks if the provided string contains at least one uppercase letter.
     *
     * @param string The string to be checked.
     * @return True if there are no uppercase letters, otherwise false.
     */
    private boolean doesNotContainUppercase(String string) {
        return !string.matches(UPPERCASE_REGEX);
    }

    /**
     * Checks if the provided username is already taken in the UserRepository.
     *
     * @param username The username to be checked.
     * @throws NonUniqueValueException If the username is already taken.
     */
    public void findNonUniqueUsername(String username) {
        if (isNonUniqueUsername(username)) {
            throw new NonUniqueValueException(
                    "An account with the chosen username already exists",
                    new ValidationErrorDetail(
                            "user",
                            "username",
                            username,
                            NON_UNIQUE_USERNAME_ERROR_MESSAGE));
        }
    }

    /**
     * Checks if the provided username already exists in the UserRepository.
     *
     * @param username The username to be checked for uniqueness.
     * @return True if the username is already taken, otherwise false.
     */
    private boolean isNonUniqueUsername(String username) {
        return userRepository.isPreexistingUsername(username);
    }

    /**
     * Generates a ValidationErrorDetail object based on the provided type and value.
     *
     * @param type  The type of validation error (username or password).
     * @param value The value that failed validation.
     * @return A ValidationErrorDetail object.
     * @throws IllegalArgumentException If the provided type is invalid.
     */
    private ValidationErrorDetail generateValidationErrorDetail(String type, String value) {
        String field;
        String errorMessage;
        boolean isNullValue = (value == null);
        boolean isBlankValue = (value != null && value.isBlank());

        switch (type) {
            case "username" -> {
                field = "username";
                errorMessage =
                        isNullValue ? NULL_FIELD_ERROR_MESSAGE
                                : isBlankValue ? BLANK_FIELD_ERROR_MESSAGE
                                : isInvalidUsernameLength(value) ? INVALID_USERNAME_LENGTH_ERROR_MESSAGE
                                : hasNonAlphanumericCharacters(value) ? NON_ALPHANUMERIC_ERROR_MESSAGE
                                : UNKNOWN_FIELD_ERROR_MESSAGE;
            }
            case "password" -> {
                field = "password";
                errorMessage =
                        isNullValue ? NULL_FIELD_ERROR_MESSAGE
                                : isBlankValue ? BLANK_FIELD_ERROR_MESSAGE
                                : isInvalidPasswordLength(value) ? INVALID_PASSWORD_LENGTH_ERROR_MESSAGE
                                : doesNotContainUppercase(value) ? NO_UPPERCASE_ERROR_MESSAGE
                                : UNKNOWN_FIELD_ERROR_MESSAGE;
            }
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        }

        return new ValidationErrorDetail("user", field, value, errorMessage);
    }

}
