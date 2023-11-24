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
import me.code.dropfolder.util.JpQueryUtil;
import me.code.dropfolder.util.UniqueNameGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service class for managing file-related operations, such as uploading, downloading, and deleting files.
 * <p>
 * This class handles interactions between users, folders, and files, ensuring proper permissions and file integrity.
 * It utilizes a combination of transaction management, query utilities, and a unique name generator for file handling.
 */
@Service
public class FileService {
    private final FileRepository fileRepository;
    private final JpQueryUtil query;
    private final UniqueNameGeneratorUtil nameGenerator;

    /**
     * Constructs a new FileService with the given dependencies.
     *
     * @param fileRepository The repository for file-related database operations.
     * @param jpQueryUtil    The utility for executing JPQL queries related to files, folders and users.
     * @param nameGenerator  The utility for generating unique names for files and folders.
     */
    @Autowired
    public FileService(FileRepository fileRepository, JpQueryUtil jpQueryUtil, UniqueNameGeneratorUtil nameGenerator) {
        this.fileRepository = fileRepository;
        this.query = jpQueryUtil;
        this.nameGenerator = nameGenerator;
    }

    /**
     * Uploads a new file to the specified folder.
     *
     * @param userId       The ID of the user initiating the upload.
     * @param folderId     The ID of the target folder for the upload.
     * @param attachedFile The file to be uploaded.
     * @return A SuccessDto indicating the result of the upload operation.
     * @throws FileUploadFailureException If the upload operation fails.
     */
    @Transactional
    public SuccessDto upload(long userId, long folderId, MultipartFile attachedFile) {
        try {
            User requestingUser = query.loadUserById(userId);
            Folder targetFolder = query.loadFolderById(folderId);

            checkUsersUploadPermission(requestingUser, targetFolder);
            File newFile = createAndSaveFile(attachedFile, targetFolder);

            return new SuccessDto(
                    HttpStatus.CREATED,
                    "Successfully uploaded a new file with name: {" + newFile.getName() + "}" +
                            " and id: {" + newFile.getId() + "}");

        } catch (Exception exception) {
            throw new FileUploadFailureException("Failed to upload file",
                    new FileUploadErrorDetail(attachedFile, exception));
        }
    }

    /**
     * Checks if the user has permission to upload a file to the specified folder.
     *
     * @param user         The user initiating the upload.
     * @param targetFolder The target folder for the upload.
     * @throws UnauthorizedFileOperationException If the user is not the owner of the target folder.
     */
    private void checkUsersUploadPermission(User user, Folder targetFolder) {
        if (query.userIsNotOwnerOfTargetFolder(user, targetFolder)) {
            throw new UnauthorizedFileOperationException("User with id: {" + user.getId() + "} is not the owner of" +
                    " folder with id: {" + targetFolder.getId() + "}");
        }
    }

    /**
     * Creates a new File from the provided MultipartFile and saves it to the specified folder.
     *
     * @param attachedFile The MultipartFile representing the content of the file to be created.
     * @param targetFolder The target folder in which the file will be saved.
     * @return The newly created File object.
     * @throws IOException If an I/O error occurs while creating or saving the file.
     */
    private File createAndSaveFile(MultipartFile attachedFile, Folder targetFolder) throws IOException {
        File file = new File(attachedFile, targetFolder);
        nameGenerator.setUniqueFileName(file);
        fileRepository.save(file);
        return file;
    }

    /**
     * Fetches a file for download based on the provided user, folder, and file IDs.
     *
     * @param userId   The ID of the user initiating the download.
     * @param folderId The ID of the folder containing the target file.
     * @param fileId   The ID of the file to be downloaded.
     * @return The File object to be downloaded.
     * @throws FileDownloadFailureException If the download operation fails.
     */
    public File fetchFileForDownload(long userId, long folderId, long fileId) {
        try {
            User requestingUser = query.loadUserById(userId);
            Folder targetFolder = query.loadFolderById(folderId);
            File targetFile = query.loadFileById(fileId);

            checkUsersDownloadPermission(requestingUser, targetFolder, targetFile);
            return query.loadFileById(fileId);

        } catch (Exception exception) {
            throw new FileDownloadFailureException("Failed to download file",
                    new FileOperationErrorDetail(exception.getMessage()));
        }
    }

    /**
     * Checks if the provided user has permission to download the specified file from the given folder.
     *
     * @param user         The user attempting to download the file.
     * @param targetFolder The folder containing the target file.
     * @param targetFile   The file to be downloaded.
     * @throws UnauthorizedFileOperationException If the user is not the owner of the file in the specified folder.
     */
    private void checkUsersDownloadPermission(User user, Folder targetFolder, File targetFile) {
        if (query.userIsNotOwnerOfTargetFile(user, targetFolder, targetFile)) {
            throw new UnauthorizedFileOperationException("User with id: {" + user.getId() + "} is not the owner of" +
                    " file with id: {" + targetFile.getId() + "}");
        }
    }

    /**
     * Deletes a file based on the provided user, folder, and file IDs.
     *
     * @param userId   The ID of the user initiating the deletion.
     * @param folderId The ID of the folder containing the target file.
     * @param fileId   The ID of the file to be deleted.
     * @return A SuccessDto indicating the result of the deletion operation.
     * @throws FileDeletionFailureException If the deletion operation fails.
     */
    @Transactional
    public SuccessDto delete(long userId, long folderId, long fileId) {
        try {
            User requestingUser = query.loadUserById(userId);
            Folder targetFolder = query.loadFolderById(folderId);
            File targetFile = query.loadFileById(fileId);

            checkUsersDeletePermission(requestingUser, targetFolder, targetFile);
            fileRepository.deleteById(fileId);

            return new SuccessDto(
                    HttpStatus.OK,
                    "Successfully deleted a file with id: {" + fileId + "}");

        } catch (Exception exception) {
            throw new FileDeletionFailureException("Failed to delete file",
                    new FileOperationErrorDetail(exception.getMessage()));
        }
    }

    /**
     * Checks if the provided user has permission to delete the specified file from the given folder.
     *
     * @param user         The user attempting to delete the file.
     * @param targetFolder The folder containing the target file.
     * @param targetFile   The file to be deleted.
     * @throws UnauthorizedFileOperationException If the user is not the owner of the file in the specified folder.
     */
    private void checkUsersDeletePermission(User user, Folder targetFolder, File targetFile) {

        if (query.userIsNotOwnerOfTargetFile(user, targetFolder, targetFile)) {
            throw new UnauthorizedFileOperationException("User with id: {" + user.getId() + "} is not the owner of" +
                    " file with id: {" + targetFile.getId() + "}");
        }
    }

}
