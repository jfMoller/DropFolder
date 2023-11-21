package me.code.dropfolder.repository;

import me.code.dropfolder.model.Folder;
import me.code.dropfolder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Folder f WHERE f.user = :user AND f.name = :name")
    boolean isPreexistingFolder(@Param("user") User user, @Param("name") String name);
}
