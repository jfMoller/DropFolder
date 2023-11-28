package me.code.dropfolder.services;

import me.code.dropfolder.dtos.UserCredentialsDto;
import me.code.dropfolder.exceptions.dtos.details.ValidationErrorDetail;
import me.code.dropfolder.exceptions.types.InvalidCredentialsException;
import me.code.dropfolder.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validator class for validating user credentials during the login process.
 * <p>
 * This class checks the validity of the provided username and password and throws
 * detailed validation error information.
 */
@Component
public class LoginValidator {
    private final UserRepository userRepository;

    /**
     * Constructs a new LoginValidator with the given UserRepository dependency.
     *
     * @param userRepository The repositories for user-related database operations.
     */
    @Autowired
    public LoginValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validates the user credentials provided in the {@link UserCredentialsDto}.
     *
     * @param dto The user credentials DTO containing username and password.
     * @throws InvalidCredentialsException If the provided credentials are invalid.
     */
    public void validateUserCredentials(UserCredentialsDto dto) {
        String username = dto.username();
        String password = dto.password();

        if (isInvalidUsername(username)) {
            throw new InvalidCredentialsException(
                    "You have entered an invalid username",
                    getValidationErrorDetail(username));
        } else if (isInvalidPassword(username, password)) {
            throw new InvalidCredentialsException(
                    "You have entered an invalid password",
                    getValidationErrorDetail());
        }
    }

    /**
     * Checks if the provided username is considered invalid based on the UserRepository.
     *
     * @param username The username to be checked for validity.
     * @return {@code true} if the username is considered invalid, {@code false} otherwise.
     */
    private boolean isInvalidUsername(String username) {
        return userRepository.isInvalidUsername(username);
    }

    /**
     * Checks if the provided password associated with the given username is considered invalid
     * based on the UserRepository.
     *
     * @param username The username associated with the password.
     * @param password The password to be checked for validity.
     * @return {@code true} if the password is considered invalid, {@code false} otherwise.
     */
    private boolean isInvalidPassword(String username, String password) {
        return userRepository.isInvalidPassword(username, password);
    }

    /**
     * Retrieves a ValidationErrorDetail object with information about an invalid password.
     *
     * @return A ValidationErrorDetail object describing the validation error for an invalid password.
     */
    private ValidationErrorDetail getValidationErrorDetail() {
        return new ValidationErrorDetail(
                "JSON",
                "password",
                "{hidden}",
                "Is not a valid password");
    }

    /**
     * Retrieves a ValidationErrorDetail object with information about an invalid username.
     *
     * @param username The invalid username for which the validation error details is generated.
     * @return A ValidationErrorDetail object describing the validation error for an invalid username.
     */
    private ValidationErrorDetail getValidationErrorDetail(String username) {
        return new ValidationErrorDetail(
                "JSON",
                "username",
                username,
                "Is not a valid username");
    }
}
