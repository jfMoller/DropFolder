package me.code.dropfolder.util;

import jakarta.transaction.Transactional;
import me.code.dropfolder.exception.type.CouldNotFindFileException;
import me.code.dropfolder.exception.type.CouldNotFindFolderException;
import me.code.dropfolder.exception.type.CouldNotFindUserException;
import me.code.dropfolder.model.File;
import me.code.dropfolder.model.Folder;
import me.code.dropfolder.model.User;
import me.code.dropfolder.repository.FileRepository;
import me.code.dropfolder.repository.FolderRepository;
import me.code.dropfolder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpQueryUtil {

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    @Autowired
    public JpQueryUtil(FileRepository fileRepository, FolderRepository folderRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }

    public boolean isUserOwnerOfTargetFolder(User user, long folderId) {
        return folderRepository.isUserOwnerOfTargetFolder(user, folderId);
    }

    public boolean isFilePartOfTargetFolder(long fileId, Folder folder) {
        return fileRepository.isFilePartOfFolder(fileId, folder);
    }

    public boolean userIsNotOwnerOfTargetFolder(User user, Folder folder) {
        return !isUserOwnerOfTargetFolder(user, folder.getId());
    }

    public boolean userIsNotOwnerOfTargetFile(User user, Folder targetFolder, File targetFile) {
        return !isUserOwnerOfTargetFolder(user, targetFolder.getId()) &&
                isFilePartOfTargetFolder(targetFile.getId(), targetFolder);
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

    public long getUserId(String username) {
        Optional<Long> id = userRepository.findUserId(username);
        if (id.isPresent()) {
            return id.get();
        } else return -1;
    }

    public void deleteUser(String username) {
        long id = getUserId(username);
        if (id != -1) {
            userRepository.deleteById(id);
        }
    }

    public File loadFileById(long fileId) throws CouldNotFindFileException {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new CouldNotFindFileException("could not find file with id: {" + fileId + "}"));
    }

    public Folder loadFolderById(long folderId) throws CouldNotFindFolderException {
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

    public boolean userHasExistingFolderByName(User user, String name) {
        return folderRepository.isPreexistingFolder(user, name);
    }

    public Folder loadFolderByUserAndName(User user, String folderName) {
        return folderRepository.findIdByUserAndFolderName(user, folderName)
                .orElseThrow(() -> new CouldNotFindFolderException(
                        "could not find folder with name: {" + folderName + "}, owned by user with id: {" + user.getId() + "}"));
    }

}
