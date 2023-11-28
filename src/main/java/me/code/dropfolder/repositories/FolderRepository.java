package me.code.dropfolder.repositories;

import me.code.dropfolder.models.Folder;
import me.code.dropfolder.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on Folder entities in the database.
 * Extends JpaRepository to inherit basic CRUD functionality.
 */
public interface FolderRepository extends JpaRepository<Folder, Long> {

    Optional<Folder> findById(long id);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Folder f WHERE f.user = :user AND f.name = :name")
    boolean isPreexistingFolder(User user, String name);

    @Query("SELECT f FROM Folder f WHERE f.user = :user AND f.name = :name")
    Optional<Folder> findIdByUserAndFolderName(User user, String name);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Folder f WHERE f.user = :user AND f.id = :folderId")
    boolean isUserOwnerOfTargetFolder(User user, long folderId);
}
