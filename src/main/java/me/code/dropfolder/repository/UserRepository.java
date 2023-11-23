package me.code.dropfolder.repository;

import me.code.dropfolder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on User entities in the database.
 * Extends JpaRepository to inherit basic CRUD functionality.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findById(long id);

    @Query("SELECT u.id FROM User u WHERE u.username = :username")
    Optional<Long> findUserId(String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username")
    boolean isPreexistingUsername(String username);

    @Query("SELECT CASE WHEN COUNT(u) = 0 THEN true ELSE false END FROM User u WHERE u.username = :username")
    boolean isInvalidUsername(String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username" +
            " AND u.password NOT LIKE :password")
    boolean isInvalidPassword(String username, String password);

}
