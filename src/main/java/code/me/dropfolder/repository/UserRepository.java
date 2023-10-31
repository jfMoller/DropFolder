package code.me.dropfolder.repository;

import code.me.dropfolder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
