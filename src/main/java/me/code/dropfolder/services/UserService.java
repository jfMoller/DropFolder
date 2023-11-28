package me.code.dropfolder.services;

import me.code.dropfolder.dtos.SuccessDto;
import me.code.dropfolder.exceptions.types.AccountRegistrationException;
import me.code.dropfolder.exceptions.types.CouldNotFindUserException;
import me.code.dropfolder.models.User;
import me.code.dropfolder.repositories.UserRepository;
import me.code.dropfolder.utils.JpQueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class responsible for user-related operations, including user registration and authentication.
 * Implements the Spring Security {@link UserDetailsService} interface for user authentication purposes.
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRegistrationValidator userRegistrationValidator;
    private final JpQueryUtil query;

    /**
     * Constructs a new instance of the UserService.
     *
     * @param userRepository            The repositories for user-related database operations.
     * @param passwordEncoder           The encoder for securing user passwords.
     * @param userRegistrationValidator The validator for checking and enforcing user registration constraints.
     */
    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserRegistrationValidator userRegistrationValidator,
                       JpQueryUtil query) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRegistrationValidator = userRegistrationValidator;
        this.query = query;
    }

    /**
     * Registers a new user with the provided username and password.
     * Performs validation checks and encrypts the password before saving the user to the database.
     *
     * @param username The username for the new user.
     * @param password The password for the new user.
     * @return A {@link SuccessDto} indicating the success of the registration process.
     * @throws AccountRegistrationException If an error occurs during the user registration process.
     */
    @Transactional
    public SuccessDto registerUser(String username, String password) {
        checkForValidationErrors(username, password);

        try {
            String encryptedPassword = passwordEncoder.encode(password);
            userRepository.save(new User(username, encryptedPassword));
            return new SuccessDto(
                    HttpStatus.CREATED,
                    "Successfully registered a new account with username: " + username);

        } catch (Exception exception) {
            throw new AccountRegistrationException("Failed to register a new account: " + exception.getMessage());
        }
    }

    /**
     * Checks for validation errors in the provided username and password using the registration validator.
     *
     * @param username The username to be validated.
     * @param password The password to be validated.
     */
    private void checkForValidationErrors(String username, String password) {
        userRegistrationValidator.findFormattingErrors(username, password);
        userRegistrationValidator.findNonUniqueUsername(username);
    }

    /**
     * Loads a user by the given username for authentication purposes.
     *
     * @param username The username of the user to load.
     * @return A {@link User} object representing the loaded user.
     * @throws CouldNotFindUserException If the specified username does not exist in the system.
     */
    @Override
    public User loadUserByUsername(String username) {
        return query.loadUserByUsername(username);
    }
}
