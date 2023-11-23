package me.code.dropfolder.service.folder;

import me.code.dropfolder.dto.SuccessDto;
import me.code.dropfolder.exception.dto.detail.FolderOperationErrorDetail;
import me.code.dropfolder.exception.type.CouldNotFindFolderException;
import me.code.dropfolder.exception.type.CouldNotFindUserException;
import me.code.dropfolder.exception.type.FolderCreationFailureException;
import me.code.dropfolder.model.Folder;
import me.code.dropfolder.model.User;
import me.code.dropfolder.repository.FolderRepository;
import me.code.dropfolder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class FolderService {
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    @Autowired
    public FolderService(FolderRepository folderRepository, UserRepository userRepository) {
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }

    public SuccessDto createFolder(long userId, String name) {
        try {
            User user = loadUserById(userId);
            String uniqueName = getUniqueFolderName(user, name);

            Folder newFolder = new Folder(uniqueName, user);
            folderRepository.save(newFolder);

            return new SuccessDto(HttpStatus.CREATED, "Successfully created a new folder with name: " + uniqueName);

        } catch (Exception exception) {
            throw new FolderCreationFailureException("Failed to new create folder",
                    new FolderOperationErrorDetail(name, exception.getMessage()));
        }
    }

    private String getUniqueFolderName(User user, String name) {
        String uniqueName = name;
        int count = 2;

        while (userHasExistingFolderByName(user, uniqueName)) {
            uniqueName = name + "_" + count;
            count++;
        }
        return uniqueName;
    }

    public boolean userHasExistingFolderByName(User user, String name) {
        return folderRepository.isPreexistingFolder(user, name);
    }

    public Folder loadFolderByUserAndName(User user, String folderName) {
        return folderRepository.findIdByUserAndFolderName(user, folderName)
                .orElseThrow(() -> new CouldNotFindFolderException(
                        "could not find folder with name: {" + folderName + "}, owned by user with id: {" + user.getId() + "}"));
    }


    public User loadUserById(long userId) throws CouldNotFindUserException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CouldNotFindUserException("could not find user with id: {" + userId + "}"));
    }
}
