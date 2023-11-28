package me.code.dropfolder.services;

import me.code.dropfolder.dtos.SuccessDto;
import me.code.dropfolder.dtos.details.EntitySuccessDetail;
import me.code.dropfolder.exceptions.dtos.details.FolderOperationErrorDetail;
import me.code.dropfolder.exceptions.types.FolderCreationFailureException;
import me.code.dropfolder.models.Folder;
import me.code.dropfolder.models.User;
import me.code.dropfolder.repositories.FolderRepository;
import me.code.dropfolder.utils.JpQueryUtil;
import me.code.dropfolder.utils.UniqueNameGeneratorUtil;
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
     * @param folderRepository The repositories for folder-related database operations.
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
                    "Successfully created a new folder",
                    new EntitySuccessDetail(newFolder, "The folder that was created"));

        } catch (Exception exception) {
            throw new FolderCreationFailureException("Failed to new create folder",
                    new FolderOperationErrorDetail(name, exception.getMessage()));
        }
    }

}
