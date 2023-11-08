package me.code.dropfolder.service.file;

import me.code.dropfolder.dto.Success;
import me.code.dropfolder.model.File;
import me.code.dropfolder.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public ResponseEntity<Success> upload(MultipartFile attachedFile) {
        // Throws exceptions if there are formatting errors
        try {
            fileRepository.save(new File(attachedFile));
            return new Success(
                    HttpStatus.CREATED,
                    "Successfully uploaded a new file with name: " + attachedFile.getOriginalFilename())
                    .toResponseEntity();

        } catch (Exception exception) {
            throw new RuntimeException("File upload failed");
        }
    }

    public File getFile(String name) {
        return fileRepository.getFile(name);
    }

}
