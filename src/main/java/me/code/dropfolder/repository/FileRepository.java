package me.code.dropfolder.repository;

import me.code.dropfolder.model.File;
import me.code.dropfolder.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on File entities in the database.
 * Extends JpaRepository to inherit basic CRUD functionality.
 */
public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findById(long id);

    @Query("SELECT f FROM File f WHERE f.folder = :folder AND f.name = :name")
    Optional<File> findByFolderAndName(Folder folder, String name);

    @Query("SELECT f.id FROM File f WHERE f.name = :fileName")
    Optional<Long> findFileId(String fileName);

    @Query("SELECT CASE WHEN COUNT(fi) > 0 THEN true ELSE false END FROM File fi WHERE fi.folder = :folder AND fi.name = :fileName")
    boolean isPreexistingFile(Folder folder, String fileName);

    @Query("SELECT CASE WHEN COUNT(fi) > 0 THEN true ELSE false END FROM File fi WHERE fi.id = :fileId AND fi.folder = :folder")
    boolean isFilePartOfFolder(long fileId, Folder folder);
}
