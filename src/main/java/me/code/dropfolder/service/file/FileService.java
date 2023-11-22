package me.code.dropfolder.service.file;

import jakarta.transaction.Transactional;
import me.code.dropfolder.dto.Success;
import me.code.dropfolder.exception.dto.UploadErrorDetail;
import me.code.dropfolder.exception.type.CouldNotFindFileException;
import me.code.dropfolder.exception.type.CouldNotFindFolderException;
import me.code.dropfolder.exception.type.FileDeletionFailureException;
import me.code.dropfolder.exception.type.FileUploadFailureException;
import me.code.dropfolder.model.File;
import me.code.dropfolder.model.Folder;
import me.code.dropfolder.model.User;
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
            User folderUser = targetFolder.getUser();

            if (isUserOwnerOfTargetFolder(userId, folderUser)) {
                File file = new File(attachedFile, targetFolder);
                setUniqueFileName(file);

                fileRepository.save(file);

                return new Success(
                        HttpStatus.CREATED,
                        "Successfully uploaded a new file with name: " + file.getName());
            } else throw new CouldNotFindFolderException(
                    "Could not find folder with id: {" + folderId + "} owned by user with id: {" + userId + "}");

        } catch (Exception exception) {
            throw new FileUploadFailureException("File upload failed",
                    new UploadErrorDetail(attachedFile, exception));
        }
    }

    public Success delete(long userId, long folderId, long fileId) throws FileUploadFailureException {
        try {
            Folder targetFolder = loadFolderById(folderId);
            User folderUser = targetFolder.getUser();

            if (isUserOwnerOfTargetFolder(userId, folderUser) && isFilePartOfTargetFolder(fileId, targetFolder)) {
                fileRepository.deleteById(fileId);

                return new Success(
                        HttpStatus.OK,
                        "Successfully deleted a file with id: {" + fileId + "}");
            } else throw new CouldNotFindFileException(
                    "Could not find file with id: {" + fileId + "} in folder with id: {" + folderId + "}" +
                            " owned by user with id: {" + userId + "}");

        } catch (Exception exception) {
            throw new FileDeletionFailureException("Failed to delete file: " + exception.getMessage());
        }
    }

    private boolean isUserOwnerOfTargetFolder(long userId, User owner) {
        return (owner.getId() == userId);
    }

    private boolean isFilePartOfTargetFolder(long fileId, Folder folder) {
        return fileRepository.isFilePartOfFolder(fileId, folder);
    }

    private void setUniqueFileName(File file) {
        String attachedFileName = file.getName();
        Folder folder = file.getFolder();
        String uniqueFileName = getUniqueFileName(folder, attachedFileName);
        file.setName(uniqueFileName);
    }

    private String getUniqueFileName(Folder folder, String fileName) {
        String uniqueFileName = fileName;
        int count = 2;

        int dotIndex = fileName.indexOf(".");
        String fileNameWithoutFileExtension = fileName.substring(0, dotIndex);
        String fileExtension = fileName.substring(dotIndex);

        while (folderHasExistingFileByName(folder, uniqueFileName)) {
            uniqueFileName = fileNameWithoutFileExtension + "_" + count + fileExtension;
            count++;
        }
        return uniqueFileName;
    }

    private boolean folderHasExistingFileByName(Folder folder, String fileName) {
        return fileRepository.isPreexistingFile(folder, fileName);
    }

    @Transactional
    public File getFile(String name) {
        return fileRepository.getFile(name);
    }

    public long getFileId(String fileName) {
        Optional<Long> id = fileRepository.findFileId(fileName);
        if (id.isPresent()) {
            return id.get();
        } else return -1;
    }

    @Transactional
    public void deleteFile(String fileName) {
        long id = getFileId(fileName);
        if (id != -1) {
            fileRepository.deleteById(id);
        }
    }

    private Folder loadFolderById(long folderId) throws CouldNotFindFolderException {
        return folderRepository.findById(folderId)
                .orElseThrow(() -> new CouldNotFindFolderException("could not find folder with id: {" + folderId + "}"));
    }

}
