package me.code.dropfolder.service.login;

import me.code.dropfolder.dto.UserCredentials;
import me.code.dropfolder.exception.dto.ValidationErrorDetail;
import me.code.dropfolder.exception.type.InvalidCredentialsException;
import me.code.dropfolder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginValidator {
    private final UserRepository userRepository;

    @Autowired
    public LoginValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUserCredentials(UserCredentials dto) throws InvalidCredentialsException {
        if (isInvalidUsername(dto.username())) {
            throw new InvalidCredentialsException(
                    "You have entered an invalid username",
                    getValidationErrorDetail(dto.username()));
        } else {
            if (isInvalidPassword(dto.username(), dto.password())) {
                throw new InvalidCredentialsException(
                        "You have entered an invalid password",
                        getValidationErrorDetail());
            }
        }
    }

    private boolean isInvalidUsername(String username) {
        return userRepository.isInvalidUsername(username);
    }

    private boolean isInvalidPassword(String username, String password) {
        return userRepository.isInvalidPassword(username, password);
    }

    private ValidationErrorDetail getValidationErrorDetail() {
        return new ValidationErrorDetail(
                "JSON",
                "password",
                "{hidden}",
                "Is not a valid password");
    }

    private ValidationErrorDetail getValidationErrorDetail(String username) {
        return new ValidationErrorDetail(
                "JSON",
                "username",
                username,
                "Is not a valid username");
    }

}
