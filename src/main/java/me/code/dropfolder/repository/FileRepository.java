package me.code.dropfolder.repository;

import me.code.dropfolder.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT f FROM File f WHERE f.name = :name")
    File getFile(@Param("name") String name);

    @Query("SELECT f.id FROM File f WHERE f.name = :name")
    Optional<Long> findFileId(@Param("name") String name);
}
