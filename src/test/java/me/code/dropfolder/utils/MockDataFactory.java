package me.code.dropfolder.utils;

import me.code.dropfolder.dtos.UserCredentialsDto;
import me.code.dropfolder.models.File;
import me.code.dropfolder.models.Folder;
import me.code.dropfolder.models.User;
import me.code.dropfolder.services.FileService;
import me.code.dropfolder.services.FolderService;
import me.code.dropfolder.services.UserService;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for creating mock data during Cucumber testing scenarios.
 */
@Component
public class MockDataFactory {

    private final UserService userService;
    private final FolderService folderService;
    private final FileService fileService;
    private final JpQueryUtil query;

    /**
     * Constructor for the MockDataFactory class.
     *
     * @param userService    The UserService used for user-related operations.
     * @param folderService  The FolderService used for folder-related operations.
     * @param fileService    The FileService used for file-related operations.
     * @param query           The JpQueryUtil used for querying data.
     */
    public MockDataFactory(
            UserService userService,
            FolderService folderService,
            FileService fileService,
            JpQueryUtil query) {
        this.userService = userService;
        this.folderService = folderService;
        this.fileService = fileService;
        this.query = query;
    }

    /**
     * Creates and registers a mock user.
     *
     * @return The created mock user.
     */
    public User createMockUser() {
        userService.registerUser("mockusername", "Mock_password");
        return userService.loadUserByUsername("mockusername");
    }

    /**
     * Creates and registers a mock user with specified credentials.
     *
     * @param username The username for the mock user.
     * @param password The password for the mock user.
     * @return The created mock user.
     */
    public User createMockUser(String username, String password) {
        userService.registerUser(username, password).toResponseEntity();
        return userService.loadUserByUsername(username);
    }

    /**
     * Creates mock credentials for a user.
     *
     * @param username The username for the mock user.
     * @param password The password for the mock user.
     * @return The created mock credentials.
     */
    public UserCredentialsDto createMockCredentials(String username, String password) {
        return new UserCredentialsDto(username, password);
    }

    /**
     * Creates and registers a mock folder for a user.
     *
     * @param mockUser   The user for whom the folder is created.
     * @param folderName The name of the mock folder.
     * @return The created mock folder.
     */
    public Folder createMockFolder(User mockUser, String folderName) {
        folderService.createFolder(mockUser.getId(), folderName).toResponseEntity();
        return query.loadFolderByUserAndName(mockUser, folderName);
    }

    /**
     * Creates and registers a mock file in a folder.
     *
     * @param userId    The ID of the user who owns the folder.
     * @param folderId  The ID of the folder where the file is uploaded.
     * @param fileName  The name of the mock file.
     * @return The created mock file.
     */
    public File createMockFile(long userId, long folderId, String fileName) {
        MultipartFile attachedFile = generateMockFile(fileName);
        fileService.upload(userId, folderId, attachedFile);

        Folder folderContainingFile = query.loadFolderById(folderId);

        return query.loadFileByFolderAndName(folderContainingFile, fileName);
    }

    /**
     * Generates a mock file for testing purposes.
     *
     * @param mockFileName The name of the mock file.
     * @return The created mock file.
     */
    public MultipartFile generateMockFile(String mockFileName) {
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
