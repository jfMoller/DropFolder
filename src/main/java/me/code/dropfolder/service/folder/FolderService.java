package me.code.dropfolder.service.folder;

import me.code.dropfolder.dto.SuccessDto;
import me.code.dropfolder.exception.dto.detail.FolderOperationErrorDetail;
import me.code.dropfolder.exception.type.FolderCreationFailureException;
import me.code.dropfolder.model.Folder;
import me.code.dropfolder.model.User;
import me.code.dropfolder.repository.FolderRepository;
import me.code.dropfolder.util.JpQueryUtil;
import me.code.dropfolder.util.UniqueNameGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service class for managing folder-related operations, such as creating folders.
 * <p>
 * This class handles the creation of folders for a specified user, ensuring unique folder names.
 */
@Service
public class FolderService {
    private final FolderRepository folderRepository;
    private final JpQueryUtil query;
    private final UniqueNameGeneratorUtil nameGenerator;

    /**
     * Constructs a new FolderService with the given dependencies.
     *
     * @param folderRepository The repository for folder-related database operations.
     * @param query            The utility for executing JPQL queries related to folders and users.
     * @param nameGenerator    The utility for generating unique names for folders and files.
     */
    @Autowired
    public FolderService(FolderRepository folderRepository, JpQueryUtil query, UniqueNameGeneratorUtil nameGenerator) {
        this.folderRepository = folderRepository;
        this.query = query;
        this.nameGenerator = nameGenerator;
    }

    /**
     * Creates a new folder for the specified user with the given name.
     *
     * @param userId The ID of the user initiating the folder creation.
     * @param name   The name of the new folder.
     * @return A SuccessDto indicating the result of the folder creation operation.
     * @throws FolderCreationFailureException If the folder creation operation fails.
     */
    public SuccessDto createFolder(long userId, String name) {
        try {
            User user = query.loadUserById(userId);
            Folder newFolder = new Folder(name, user);
            nameGenerator.setUniqueFolderName(user, newFolder);

            folderRepository.save(newFolder);

            return new SuccessDto(HttpStatus.CREATED,
                    "Successfully created a new folder with name: " + newFolder.getName());

        } catch (Exception exception) {
            throw new FolderCreationFailureException("Failed to new create folder",
                    new FolderOperationErrorDetail(name, exception.getMessage()));
        }
    }

}
