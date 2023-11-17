package me.code.dropfolder.service.folder;

import me.code.dropfolder.dto.Success;
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

    public Success createFolder(String name, long user_id) {
        User owner = userRepository.findUserById(user_id);
        Folder newFolder = new Folder(name, owner);
        folderRepository.save(newFolder);

        return new Success(HttpStatus.CREATED, "Successfully created a new folder with name: " + name);
    }
}
