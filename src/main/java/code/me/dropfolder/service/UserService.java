package code.me.dropfolder.service;

import code.me.dropfolder.dto.Success;
import code.me.dropfolder.exception.type.AccountRegistrationException;
import code.me.dropfolder.model.User;
import code.me.dropfolder.repository.UserRepository;
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

    @Autowired
    public UserService(UserRepository userRepository, UserRegistrationValidator userRegistrationValidator) {
        this.userRepository = userRepository;
        this.userRegistrationValidator = userRegistrationValidator;
    }

    @Transactional
    public ResponseEntity<Success> signUp(String username, String password) {

        // Throws exceptions if there are formatting errors
        userRegistrationValidator.findFormattingErrors(username, password);

        // Throws an exception if the chosen username already exists
        userRegistrationValidator.findNonUniqueUsername(username);

        // Tries to create a and save a new user
        try {
            User newUser = new User(username, password);
            userRepository.save(newUser);

            boolean isUserRegistered = isExistingUser(newUser.getId());
            if (isUserRegistered) {
                return new Success(
                        HttpStatus.CREATED,
                        "Successfully registered a new account with username: " + newUser.getUsername())
                        .toResponseEntity();
            } else {
                throw new AccountRegistrationException(
                        "account with username {" + newUser.getUsername() + "} could not be saved in the user-repository");
            }
        } catch (Exception exception) {
            throw new AccountRegistrationException("Failed to register a new account: " + exception.getMessage());
        }
    }

    public ResponseEntity<Success> login(String username, String password) {
        return null;
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
