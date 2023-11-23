package me.code.dropfolder.service.file;

import jakarta.transaction.Transactional;
import me.code.dropfolder.dto.SuccessDto;
import me.code.dropfolder.exception.dto.detail.FileOperationErrorDetail;
import me.code.dropfolder.exception.dto.detail.FileUploadErrorDetail;
import me.code.dropfolder.exception.type.*;
import me.code.dropfolder.model.File;
import me.code.dropfolder.model.Folder;
import me.code.dropfolder.model.User;
import me.code.dropfolder.repository.FileRepository;
import me.code.dropfolder.repository.FolderRepository;
import me.code.dropfolder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    @Autowired
    public FileService(FileRepository fileRepository, FolderRepository folderRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }

    public SuccessDto upload(long userId, long folderId, MultipartFile attachedFile) throws FileUploadFailureException {
        try {
            User requestingUser = loadUserById(userId);
            Folder targetFolder = loadFolderById(folderId);
            long targetFolderId = targetFolder.getId();

            if (isUserOwnerOfTargetFolder(requestingUser, targetFolderId)) {
                File file = new File(attachedFile, targetFolder);
                setUniqueFileName(file);

                fileRepository.save(file);

                return new SuccessDto(
                        HttpStatus.CREATED,
                        "Successfully uploaded a new file with name: " + file.getName());
            } else throw new CouldNotFindFolderException(
                    "Could not find folder with id: {" + folderId + "} owned by user with id: {" + userId + "}");

        } catch (Exception exception) {
            throw new FileUploadFailureException("Failed to upload file",
                    new FileUploadErrorDetail(attachedFile, exception));
        }
    }

    public File fetchFileForDownload(long userId, long folderId, long fileId) {
        try {
            User requestingUser = loadUserById(userId);
            Folder targetFolder = loadFolderById(folderId);
            long targetFolderId = targetFolder.getId();

            if (isUserOwnerOfTargetFolder(requestingUser, targetFolderId) &&
                    isFilePartOfTargetFolder(fileId, targetFolder)) {
                return loadFileById(fileId);

            } else throw new CouldNotFindFileException(
                    "Could not find file with id: {" + fileId + "} in folder with id: {" + folderId + "}" +
                            " owned by user with id: {" + userId + "}");

        } catch (Exception exception) {
            throw new FileDownloadFailureException("Failed to download file",
                    new FileOperationErrorDetail(exception.getMessage()));
        }
    }

    public SuccessDto delete(long userId, long folderId, long fileId) throws FileUploadFailureException {
        try {
            User requestingUser = loadUserById(userId);
            Folder targetFolder = loadFolderById(folderId);
            long targetFolderId = targetFolder.getId();

            if (isUserOwnerOfTargetFolder(requestingUser, targetFolderId) &&
                    isFilePartOfTargetFolder(fileId, targetFolder)) {
                fileRepository.deleteById(fileId);

                return new SuccessDto(
                        HttpStatus.OK,
                        "Successfully deleted a file with id: {" + fileId + "}");
            } else throw new CouldNotFindFileException(
                    "Could not find file with id: {" + fileId + "} in folder with id: {" + folderId + "}" +
                            " owned by user with id: {" + userId + "}");

        } catch (Exception exception) {
            throw new FileDownloadFailureException("Failed to delete file",
                    new FileOperationErrorDetail(exception.getMessage()));
        }
    }

    public boolean isUserOwnerOfTargetFolder(User user, long folderId) {
        return folderRepository.isUserOwnerOfTargetFolder(user, folderId);
    }

    public boolean isFilePartOfTargetFolder(long fileId, Folder folder) {
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

    public boolean folderHasExistingFileByName(Folder folder, String fileName) {
        return fileRepository.isPreexistingFile(folder, fileName);
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

    private File loadFileById(long fileId) throws CouldNotFindFileException {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new CouldNotFindFileException("could not find file with id: {" + fileId + "}"));
    }

    private Folder loadFolderById(long folderId) throws CouldNotFindFolderException {
        return folderRepository.findById(folderId)
                .orElseThrow(() -> new CouldNotFindFolderException("could not find folder with id: {" + folderId + "}"));
    }

    public User loadUserById(long userId) throws CouldNotFindUserException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CouldNotFindUserException("Could not find user with id: " + userId));
    }

    @Transactional
    public File loadFileByFolderAndName(Folder folder, String fileName) throws CouldNotFindFileException {
        return fileRepository.findByFolderAndName(folder, fileName)
                .orElseThrow(() -> new CouldNotFindFileException(
                        "could not find file with folder_id: {" + folder.getId() + "} and name: {" + fileName + "}"));
    }

}
