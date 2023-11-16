package me.code.dropfolder.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Folder> folders;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }
}