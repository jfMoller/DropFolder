package me.code.dropfolder.cucumberglue;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import me.code.dropfolder.controller.LoginController;
import me.code.dropfolder.dto.Success;
import me.code.dropfolder.dto.UserCredentials;
import me.code.dropfolder.exception.type.CouldNotFindFileException;
import me.code.dropfolder.model.File;
import me.code.dropfolder.model.Folder;
import me.code.dropfolder.model.User;
import me.code.dropfolder.service.file.FileService;
import me.code.dropfolder.service.folder.FolderService;
import me.code.dropfolder.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class FileDeletionFeatureTest {

    private final LoginController loginController;
    private final UserService userService;
    private final FolderService folderService;
    private final FileService fileService;

    private HttpStatus responseEntityStatus;
    private User mockUser;
    private Folder mockFolder;
    private File mockFile;
    private MultipartFile attachedMockFile;

    public FileDeletionFeatureTest(
            LoginController loginController,
            UserService userService,
            FolderService folderService,
            FileService fileService) {
        this.loginController = loginController;
        this.userService = userService;
        this.folderService = folderService;
        this.fileService = fileService;

    }

    @After("@cleanupDeletionData")
    public void cleanUpMockData() {
        userService.deleteUser(mockUser.getUsername());
    }

    @Given("the user already has an account with username {string} and password {string}")
    public void theUserHasAnAccountWithUsernameAndPassword(String username, String password) {
        ResponseEntity<Success> responseEntity = userService.registerUser(username, password).toResponseEntity();
        responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        assertEquals(HttpStatus.CREATED, responseEntityStatus);

        // Instantiate registered mock user for re-use and post test deletion
        mockUser = userService.loadUserByUsername(username);

    }

    @Given("the user already has a folder with name {string}")
    public void theUserHasAFolderWithName(String folderName) {
        ResponseEntity<Success> responseEntity = folderService.createFolder(mockUser.getId(), folderName).toResponseEntity();
        responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        assertEquals(HttpStatus.CREATED, responseEntityStatus);

        // Instantiate created folder for re-use
        mockFolder = folderService.loadFolderByUserAndName(mockUser, folderName);
    }

    @Given("the user already has a file with name {string} in their folder with name {string}")
    public void theUserAlreadyHasAFileWithNameInTheirFolderWithName(String fileName, String folderName) {
        long userId = mockUser.getId();
        long folderId = mockFolder.getId();
        attachedMockFile = getMockFile(fileName);

        ResponseEntity<Success> responseEntity = fileService.upload(userId, folderId, attachedMockFile).toResponseEntity();
        responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        assertEquals(HttpStatus.CREATED, responseEntityStatus);
        assertEquals(folderName, mockFolder.getName());

    }

    @And("the user is already logged in with username {string} and password {string}")
    public void theUserIsLoggedInWithUsernameAndPassword(String username, String password) {
        UserCredentials dto = new UserCredentials(username, password);
        ResponseEntity<Success> responseEntity = loginController.login(dto);
        responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        assertEquals(HttpStatus.OK, responseEntityStatus);
    }

    @When("the user deletes the file with name {string}")
    public void theUserDeletesTheFileWithName(String fileName) {
        // Instantiate created file for re-use
        mockFile = fileService.loadFileByFolderAndName(mockFolder, fileName);

        fileService.deleteFile(fileName);

        assertThrows(CouldNotFindFileException.class, () -> fileService.loadFileByFolderAndName(mockFolder, fileName));
    }

    @Then("the file should be successfully deleted from the users folder")
    public void theFileShouldBeSuccessfullyDeletedFromTheUsersFolder() {
        long mockFileId = mockFile.getId();

        assertFalse(fileService.isFilePartOfTargetFolder(mockFileId, mockFolder));
    }

    private MultipartFile getMockFile(String mockFileName) {
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
