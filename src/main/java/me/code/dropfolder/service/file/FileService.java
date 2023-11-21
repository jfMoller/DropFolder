package me.code.dropfolder.service.file;

import jakarta.transaction.Transactional;
import me.code.dropfolder.dto.Success;
import me.code.dropfolder.exception.dto.UploadErrorDetail;
import me.code.dropfolder.exception.type.CouldNotFindFolderException;
import me.code.dropfolder.exception.type.FileUploadFailureException;
import me.code.dropfolder.model.File;
import me.code.dropfolder.model.Folder;
import me.code.dropfolder.repository.FileRepository;
import me.code.dropfolder.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    @Autowired
    public FileService(FileRepository fileRepository, FolderRepository folderRepository) {
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
    }

    public Success upload(long userId, long folderId, MultipartFile attachedFile) throws FileUploadFailureException {

        try {
            Folder targetFolder = loadFolderById(folderId);
            boolean isUserOwnerOfTargetFolder = (targetFolder.getUser().getId() == userId);

            if (isUserOwnerOfTargetFolder) {

                fileRepository.save(new File(attachedFile, targetFolder));

                return new Success(
                        HttpStatus.CREATED,
                        "Successfully uploaded a new file with name: " + attachedFile.getOriginalFilename());
            } else throw new CouldNotFindFolderException(
                    "Could not find folder with id: {" + folderId + "} owned by user with id: {" + userId + "}");

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

    public Folder loadFolderById(long folderId) throws CouldNotFindFolderException {
        return folderRepository.findById(folderId)
                .orElseThrow(() -> new CouldNotFindFolderException("could not find folder with id: {" + folderId + "}"));
    }

}
