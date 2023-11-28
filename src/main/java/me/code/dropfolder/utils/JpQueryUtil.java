package me.code.dropfolder.utils;

import jakarta.transaction.Transactional;
import me.code.dropfolder.exceptions.types.CouldNotFindFileException;
import me.code.dropfolder.exceptions.types.CouldNotFindFolderException;
import me.code.dropfolder.exceptions.types.CouldNotFindUserException;
import me.code.dropfolder.models.File;
import me.code.dropfolder.models.Folder;
import me.code.dropfolder.models.User;
import me.code.dropfolder.repositories.FileRepository;
import me.code.dropfolder.repositories.FolderRepository;
import me.code.dropfolder.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Utility class for performing JPQL queries related to file, folder, and user entities.
 * Provides methods for various database queries and operations.
 */
@Component
public class JpQueryUtil {

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a new instance of JpQueryUtil with the specified repositories.
     *
     * @param fileRepository   The repositories for file entities.
     * @param folderRepository The repositories for folder entities.
     * @param userRepository   The repositories for user entities.
     */
    @Autowired
    public JpQueryUtil(FileRepository fileRepository, FolderRepository folderRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }

    /**
     * Checks whether the specified user is the owner of the target folder identified by its unique identifier.
     *
     * @param user     The user to be checked as the owner.
     * @param folderId The unique identifier of the target folder.
     * @return {@code true} if the user is the owner of the folder, {@code false} otherwise.
     */
    public boolean isUserOwnerOfTargetFolder(User user, long folderId) {
        return folderRepository.isUserOwnerOfTargetFolder(user, folderId);
    }

    /**
     * Checks whether the file identified by its ID is part of the target folder.
     *
     * @param fileId The unique identifier of the file to be checked.
     * @param folder The target folder to check for file inclusion.
     * @return {@code true} if the file is part of the folder, {@code false} otherwise.
     */
    public boolean isFilePartOfTargetFolder(long fileId, Folder folder) {
        return fileRepository.isFilePartOfFolder(fileId, folder);
    }

    /**
     * Checks whether the given user is not the owner of the target folder.
     *
     * @param user   The user to check ownership for.
     * @param folder The target folder to check ownership against.
     * @return {@code true} if the user is not the owner of the folder, {@code false} otherwise.
     */
    public boolean userIsNotOwnerOfTargetFolder(User user, Folder folder) {
        return !isUserOwnerOfTargetFolder(user, folder.getId());
    }

    /**
     * Checks whether the given user is not the owner of the target file.
     *
     * @param user         The user to check ownership for.
     * @param targetFolder The target folder to check ownership against.
     * @param targetFile   The target file to check inclusion in the folder.
     * @return {@code true} if the user is not the owner of the folder and the file is part of the folder, {@code false} otherwise.
     */
    public boolean userIsNotOwnerOfTargetFile(User user, Folder targetFolder, File targetFile) {
        return !isUserOwnerOfTargetFolder(user, targetFolder.getId()) &&
                isFilePartOfTargetFolder(targetFile.getId(), targetFolder);
    }

    /**
     * Checks whether the target folder has an existing file with the specified file name.
     *
     * @param folder   The target folder to check for file existence.
     * @param fileName The name of the file to check for existence.
     * @return {@code true} if the folder has an existing file with the given name, {@code false} otherwise.
     */
    public boolean folderHasExistingFileByName(Folder folder, String fileName) {
        return fileRepository.isPreexistingFile(folder, fileName);
    }

    /**
     * Retrieves the ID of the file with the specified file name.
     *
     * @param fileName The name of the file to retrieve the identifier for.
     * @return The unique identifier of the file if it exists, or {@code -1} if not found.
     */
    public long getFileId(String fileName) {
        Optional<Long> id = fileRepository.findFileId(fileName);
        if (id.isPresent()) {
            return id.get();
        } else return -1;
    }

    /**
     * Deletes the file with the specified file name.
     *
     * @param fileName The name of the file to be deleted.
     */
    @Transactional
    public void deleteFile(String fileName) {
        long id = getFileId(fileName);
        if (id != -1) {
            fileRepository.deleteById(id);
        }
    }

    /**
     * Retrieves the ID of the user with the specified username.
     *
     * @param username The username of the user to retrieve the identifier for.
     * @return The unique identifier of the user if it exists, or {@code -1} if not found.
     */
    public long getUserId(String username) {
        Optional<Long> id = userRepository.findUserId(username);
        if (id.isPresent()) {
            return id.get();
        } else return -1;
    }

    /**
     * Deletes the user with the specified username.
     *
     * @param username The username of the user to be deleted.
     * @throws CouldNotFindUserException If the user with the specified username and id could not be found.
     */
    public void deleteUser(String username) {
        long id = getUserId(username);
        if (id != -1) {
            userRepository.deleteById(id);
        } else throw new CouldNotFindUserException(
                "Could not find user with username: {" + username + "} and id: {" + id + "}");
    }

    /**
     * Retrieves the file with the specified ID.
     *
     * @param fileId The unique identifier of the file to retrieve.
     * @return The file with the specified identifier.
     * @throws CouldNotFindFileException If the file with the specified identifier could not be found.
     */
    public File loadFileById(long fileId) throws CouldNotFindFileException {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new CouldNotFindFileException("could not find file with id: {" + fileId + "}"));
    }

    /**
     * Retrieves the folder with the specified ID.
     *
     * @param folderId The unique identifier of the folder to retrieve.
     * @return The folder with the specified identifier.
     * @throws CouldNotFindFolderException If the folder with the specified identifier could not be found.
     */
    public Folder loadFolderById(long folderId) throws CouldNotFindFolderException {
        return folderRepository.findById(folderId)
                .orElseThrow(() -> new CouldNotFindFolderException("could not find folder with id: {" + folderId + "}"));
    }

    /**
     * Retrieves the user with the specified ID.
     *
     * @param userId The unique identifier of the user to retrieve.
     * @return The user with the specified identifier.
     * @throws CouldNotFindUserException If the user with the specified identifier could not be found.
     */
    public User loadUserById(long userId) throws CouldNotFindUserException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CouldNotFindUserException("Could not find user with id: " + userId));
    }

    /**
     * Retrieves the user with the specified username.
     *
     * @param username The username of the user to retrieve.
     * @return The user with the specified username.
     * @throws CouldNotFindUserException If the user with the specified username could not be found.
     */
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CouldNotFindUserException("Could not find user with username: " + username));
    }

    /**
     * Retrieves the file in the specified folder with the given name.
     *
     * @param folder   The folder to search for the file.
     * @param fileName The name of the file to retrieve.
     * @return The file with the specified name in the specified folder.
     * @throws CouldNotFindFileException If the file with the specified name in the folder could not be found.
     */
    @Transactional
    public File loadFileByFolderAndName(Folder folder, String fileName) throws CouldNotFindFileException {
        return fileRepository.findByFolderAndName(folder, fileName)
                .orElseThrow(() -> new CouldNotFindFileException(
                        "could not find file with folder_id: {" + folder.getId() + "} and name: {" + fileName + "}"));
    }

    /**
     * Checks if the user has an existing folder with the specified name.
     *
     * @param user The user to check for folder existence.
     * @param name The name of the folder to check for existence.
     * @return {@code true} if the user has an existing folder with the given name, {@code false} otherwise.
     */
    public boolean userHasExistingFolderByName(User user, String name) {
        return folderRepository.isPreexistingFolder(user, name);
    }

    /**
     * Retrieves the folder owned by the specified user with the given name.
     *
     * @param user       The user who owns the folder.
     * @param folderName The name of the folder to retrieve.
     * @return The folder owned by the specified user with the given name.
     * @throws CouldNotFindFolderException If the folder with the specified name owned by the user could not be found.
     */
    public Folder loadFolderByUserAndName(User user, String folderName) {
        return folderRepository.findIdByUserAndFolderName(user, folderName)
                .orElseThrow(() -> new CouldNotFindFolderException(
                        "could not find folder with name: {" + folderName + "}, owned by user with id: {" + user.getId() + "}"));
    }

}
