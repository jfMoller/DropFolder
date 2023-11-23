package me.code.dropfolder.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a folder in the system.
 */
@Entity
@Table(name = "folders")
@Getter
@Setter
@NoArgsConstructor
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    /**
     * The user to whom the folder belongs.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The list of files contained in the folder.
     * <p>
     * Important Note: Since each folder has a CascadeType.ALL association with its files,
     * removing a user will result in the removal of all folders and files associated with that user.
     */
    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files;

    /**
     * Constructor for creating a Folder entity with a specified name and associated user.
     *
     * @param name The name of the folder.
     * @param user The user to whom the folder belongs.
     */
    public Folder(String name, User user) {
        this.name = name;
        this.user = user;
        this.files = new ArrayList<>();
    }

}
