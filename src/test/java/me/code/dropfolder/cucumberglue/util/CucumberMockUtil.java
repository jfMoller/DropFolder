package me.code.dropfolder.cucumberglue.util;

import me.code.dropfolder.model.Folder;
import me.code.dropfolder.model.User;
import me.code.dropfolder.service.folder.FolderService;
import me.code.dropfolder.service.user.UserService;
import me.code.dropfolder.util.JpQueryUtil;
import org.springframework.stereotype.Component;

@Component
public class CucumberMockUtil {

    private final UserService userService;
    private final FolderService folderService;
    private final JpQueryUtil query;

    public CucumberMockUtil(
            UserService userService,
            FolderService folderService,
            JpQueryUtil query) {
        this.userService = userService;
        this.folderService = folderService;
        this.query = query;
    }

    public User createMockUser() {
        userService.registerUser("mockUser", "mockPassword").toResponseEntity();
        return userService.loadUserByUsername("mockUser");
    }

    public Folder createMockFolder(User mockUser, String folderName) {
        folderService.createFolder(mockUser.getId(), folderName).toResponseEntity();
        return query.loadFolderByUserAndName(mockUser, folderName);
    }
}
