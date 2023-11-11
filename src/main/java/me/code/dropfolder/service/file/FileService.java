package me.code.dropfolder.service.file;

import jakarta.transaction.Transactional;
import me.code.dropfolder.dto.Success;
import me.code.dropfolder.exception.dto.UploadErrorDetail;
import me.code.dropfolder.exception.type.FileUploadFailureException;
import me.code.dropfolder.model.File;
import me.code.dropfolder.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class FileService {
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public Success upload(MultipartFile attachedFile) throws FileUploadFailureException {
        // Throws exceptions if there are formatting errors
        try {
            fileRepository.save(new File(attachedFile));
            return new Success(
                    HttpStatus.CREATED,
                    "Successfully uploaded a new file with name: " + attachedFile.getOriginalFilename());

        } catch (Exception exception) {
            throw new FileUploadFailureException("File upload failed",
                    new UploadErrorDetail(attachedFile, exception));
        }
    }

    @Transactional
    public File getFile(String name) {
        return fileRepository.getFile(name);
    }

    public long getFileId(String name) {
        Optional<Long> id = fileRepository.findFileId(name);
        if (id.isPresent()) {
            return id.get();
        } else return -1;
    }

    @Transactional
    public void deleteFile(String username) {
        long id = getFileId(username);
        if (id != -1) {
            fileRepository.deleteById(id);
        }
    }

}
