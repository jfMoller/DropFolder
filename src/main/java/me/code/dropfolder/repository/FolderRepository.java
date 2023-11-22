package me.code.dropfolder.repository;

import me.code.dropfolder.model.Folder;
import me.code.dropfolder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    Optional<Folder> findById(long id);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Folder f WHERE f.user = :user AND f.name = :name")
    boolean isPreexistingFolder(@Param("user") User user, @Param("name") String name);

    @Query("SELECT f FROM Folder f WHERE f.user = :user AND f.name = :name")
    Optional<Folder> findIdByUserAndFolderName (User user, String name);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Folder f WHERE f.user = :user AND f.id = :folderId")
    boolean isUserOwnerOfTargetFolder(User user, long folderId);
}
