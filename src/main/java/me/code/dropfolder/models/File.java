package me.code.dropfolder.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Entity class representing a file in the system.
 */
@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "size", nullable = false)
    private Long size;

    @Lob
    @Column(name = "data", nullable = false)
    private byte[] data;

    /**
     * The folder to which the file belongs, all files have a column with "folder_id".
     */
    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    /**
     * Constructor for creating a File entity from a multipart file and connecting it to a folder.
     *
     * @param file   The multipart file from which to create the File entity.
     * @param folder The folder to which the file belongs.
     * @throws IOException If an I/O error occurs while reading the file data.
     */
    public File(MultipartFile file, Folder folder) throws IOException {
        this.name = file.getOriginalFilename();
        this.contentType = file.getContentType();
        this.size = file.getSize();
        this.data = file.getBytes();
        this.folder = folder;
    }

}
