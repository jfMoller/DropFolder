package me.code.dropfolder.service.user;

import me.code.dropfolder.dto.Success;
import me.code.dropfolder.exception.type.*;
import me.code.dropfolder.model.User;
import me.code.dropfolder.repository.UserRepository;
import me.code.dropfolder.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRegistrationValidator userRegistrationValidator;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserRegistrationValidator userRegistrationValidator,
                       JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRegistrationValidator = userRegistrationValidator;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Transactional
    public Success registerUser(String username, String password)
            throws UsernameValidationException, PasswordValidationException,
            NonUniqueValueException, AccountRegistrationException {

        // Throws exceptions if there are formatting errors
        userRegistrationValidator.findFormattingErrors(username, password);

        // Throws an exception if the chosen username already exists
        userRegistrationValidator.findNonUniqueUsername(username);

        try {
            String encryptedPassword = passwordEncoder.encode(password);
            userRepository.save(new User(username, encryptedPassword));
            return new Success(
                    HttpStatus.CREATED,
                    "Successfully registered a new account with username: " + username);

        } catch (Exception exception) {
            throw new AccountRegistrationException("Failed to register a new account: " + exception.getMessage());
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

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CouldNotFindUserException("Could not find user with username: " + username));
    }
}
