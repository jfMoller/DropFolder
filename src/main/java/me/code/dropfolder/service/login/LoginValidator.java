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

    public void validateUsername(UserCredentials dto) {
        boolean isInvalidUsername = userRepository.isInvalidUsername(dto.username(), dto.password());
        if (isInvalidUsername) {
            throw new InvalidCredentialsException(
                    "You've entered an invalid username",
                    new ValidationErrorDetail(
                            dto.getClass().getSimpleName(),
                            "username",
                            dto.password(),
                            "Is not a valid username for this account"));
        }
    }

    public void validatePassword(UserCredentials dto) {
        boolean isInvalidUsername = userRepository.isInvalidPassword(dto.username(), dto.password());
        if (isInvalidUsername) {
            throw new InvalidCredentialsException(
                    "You've entered an invalid password",
                    new ValidationErrorDetail(
                            dto.getClass().getSimpleName(),
                            "password",
                            dto.password(),
                            "Is not a valid password for this account"));
        }
    }
}
