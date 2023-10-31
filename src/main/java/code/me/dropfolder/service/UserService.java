package code.me.dropfolder.service;

import code.me.dropfolder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Object> signUp(String email, String password) {
        return null;
    }

    public ResponseEntity<Object> login(String email, String password) {
        return null;
    }

}
