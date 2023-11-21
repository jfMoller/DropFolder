package me.code.dropfolder.repository;

import me.code.dropfolder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
    User findUser(@Param("username") String username, @Param("password") String password);

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    User findUserById(@Param("id") long id);

    @Query("SELECT u.id FROM User u WHERE u.username = :username")
    Optional<Long> findUserId(@Param("username") String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username")
    boolean isPreexistingUsername(@Param("username") String username);

    @Query("SELECT CASE WHEN COUNT(u) = 0 THEN true ELSE false END FROM User u WHERE u.username = :username")
    boolean isInvalidUsername(@Param("username") String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username" +
            " AND u.password NOT LIKE :password")
    boolean isInvalidPassword(@Param("username") String username, @Param("password") String password);

}
