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

@Service
public class FolderService {
    private final FolderRepository folderRepository;
    private final JpQueryUtil query;
    private final UniqueNameGeneratorUtil nameGenerator;

    @Autowired
    public FolderService(FolderRepository folderRepository, JpQueryUtil query, UniqueNameGeneratorUtil nameGenerator) {
        this.folderRepository = folderRepository;
        this.query = query;
        this.nameGenerator = nameGenerator;
    }

    public SuccessDto createFolder(long userId, String name) {
        try {
            User user = query.loadUserById(userId);
            String uniqueName = nameGenerator.getUniqueFolderName(user, name);

            Folder newFolder = new Folder(uniqueName, user);
            folderRepository.save(newFolder);

            return new SuccessDto(HttpStatus.CREATED, "Successfully created a new folder with name: " + uniqueName);

        } catch (Exception exception) {
            throw new FolderCreationFailureException("Failed to new create folder",
                    new FolderOperationErrorDetail(name, exception.getMessage()));
        }
    }

}
