package me.code.dropfolder.service.user;

import me.code.dropfolder.security.JwtTokenProvider;
import me.code.dropfolder.dto.Auth;
import me.code.dropfolder.dto.Success;
import me.code.dropfolder.exception.type.*;
import me.code.dropfolder.model.User;
import me.code.dropfolder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserRegistrationValidator userRegistrationValidator,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRegistrationValidator = userRegistrationValidator;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRegistrationValidator = new UserRegistrationValidator(userRepository);
        this.jwtTokenProvider = new JwtTokenProvider();
    }

    @Transactional
    public Success registerUser(String username, String password)
            throws UsernameFormattingException, PasswordFormattingException,
            NonUniqueValueException, AccountRegistrationException {

        // Throws exceptions if there are formatting errors
        userRegistrationValidator.findFormattingErrors(username, password);

        // Throws an exception if the chosen username already exists
        userRegistrationValidator.findNonUniqueUsername(username);

        try {
            userRepository.save(new User(username, password));
            return new Success(
                    HttpStatus.CREATED,
                    "Successfully registered a new account with username: " + username);

        } catch (Exception exception) {
            throw new AccountRegistrationException("Failed to register a new account: " + exception.getMessage());
        }
    }

    public Auth login(String username, String password) throws LoginFailureException {
        try {
            User user = userRepository.findUser(username, password);
            String token = jwtTokenProvider.generateToken(user);
            return new Auth(HttpStatus.OK, "Login successful", token);

        } catch (Exception exception) {
            throw new LoginFailureException("Login failed; double-check your username and password and try again");
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find user with username: " + username));
        return userDetails;
    }
}
