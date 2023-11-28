package me.code.dropfolder.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Entity class representing a user in the system.
 * <p>
 * The User class implements the UserDetails interface from Spring Security, which is user for authentication and authorization purposes.
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    /**
     * A list of folders belonging to the user.
     * <p>
     * Important Note: Since each user has a CascadeType.ALL & orphan removal association with its folders,
     * removing a user will result in the removal of all folders and by extension all files belonging to that user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folder> folders;

    /**
     * Constructs a new User instance with the provided username and password.
     *
     * @param username The unique username of the user.
     * @param password The password associated with the user's account.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.folders = new ArrayList<>();
    }

    /**
     * Returns the authorities granted to the user. In this case, a single role "ROLE_USER" is assigned to every user.
     *
     * @return A collection of GrantedAuthority representing the user's roles.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
