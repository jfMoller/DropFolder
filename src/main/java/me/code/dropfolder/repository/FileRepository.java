package me.code.dropfolder.repository;

import me.code.dropfolder.model.File;
import me.code.dropfolder.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT f FROM File f WHERE f.name = :name")
    File getFile(@Param("name") String name);

    @Query("SELECT f.id FROM File f WHERE f.name = :fileName")
    Optional<Long> findFileId(@Param("fileName") String fileName);

    @Query("SELECT CASE WHEN COUNT(fi) > 0 THEN true ELSE false END FROM File fi WHERE fi.folder = :folder AND fi.name = :fileName")
    boolean isPreexistingFile(Folder folder, @Param("fileName") String fileName);

    @Query("SELECT CASE WHEN COUNT(fi) > 0 THEN true ELSE false END FROM File fi WHERE fi.id = :fileId AND fi.folder = :folder")
    boolean isFilePartOfFolder(@Param("fileId") long fileId, Folder folder);
}
