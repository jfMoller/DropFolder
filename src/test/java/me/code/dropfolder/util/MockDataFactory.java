package me.code.dropfolder.util;

import me.code.dropfolder.dto.UserCredentialsDto;
import me.code.dropfolder.model.Folder;
import me.code.dropfolder.model.User;
import me.code.dropfolder.service.folder.FolderService;
import me.code.dropfolder.service.user.UserService;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class MockDataFactory {

    private final UserService userService;
    private final FolderService folderService;
    private final JpQueryUtil query;

    public MockDataFactory(
            UserService userService,
            FolderService folderService,
            JpQueryUtil query) {
        this.userService = userService;
        this.folderService = folderService;
        this.query = query;
    }

    public User createMockUser() {
        userService.registerUser("mockusername", "Mock_password").toResponseEntity();
        return userService.loadUserByUsername("mockusername");
    }

    public UserCredentialsDto createMockCredentials(String username, String password) {
        return new UserCredentialsDto(username, password);
    }

    public Folder createMockFolder(User mockUser, String folderName) {
        folderService.createFolder(mockUser.getId(), folderName).toResponseEntity();
        return query.loadFolderByUserAndName(mockUser, folderName);
    }

    public MultipartFile getMockFile(String mockFileName) {
        Path filePath = Paths.get("src/test/resources/mockfiles/" + mockFileName);
        byte[] content;
        String contentType = "mock_data/" + mockFileName;

        try {
            content = Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new MockMultipartFile(mockFileName, mockFileName, contentType, content);
    }
}
