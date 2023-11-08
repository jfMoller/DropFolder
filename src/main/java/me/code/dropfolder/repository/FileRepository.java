package me.code.dropfolder.repository;

import me.code.dropfolder.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT f FROM File f WHERE f.name = :name")
    File getFile(@Param("name") String name);
}
