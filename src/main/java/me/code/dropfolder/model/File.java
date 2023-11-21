package me.code.dropfolder.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    public File(MultipartFile file, Folder folder) throws IOException {
        this.name = file.getOriginalFilename();
        this.contentType = file.getContentType();
        this.size = file.getSize();
        this.data = file.getBytes();
        this.folder = folder;
    }

}
