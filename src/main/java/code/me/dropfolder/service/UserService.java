package code.me.dropfolder.service;

import code.me.dropfolder.dto.Success;
import code.me.dropfolder.model.User;
import code.me.dropfolder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<Success> signUp(String username, String password) {
        User newUser = new User(username, password);
        userRepository.save(newUser);
        if (isExistingUser(newUser.getId())) {
            return ResponseEntity.ok(
                    new Success("Account created successfully with username: " + newUser.getUsername()));
        }
        return null;
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
