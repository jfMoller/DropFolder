package me.code.dropfolder.cucumberglue;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import me.code.dropfolder.controller.LoginController;
import me.code.dropfolder.dto.SuccessDto;
import me.code.dropfolder.dto.UserCredentialsDto;
import me.code.dropfolder.model.File;
import me.code.dropfolder.model.Folder;
import me.code.dropfolder.model.User;
import me.code.dropfolder.service.file.FileService;
import me.code.dropfolder.service.folder.FolderService;
import me.code.dropfolder.service.user.UserService;
import me.code.dropfolder.util.JpQueryUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileUploadFeatureTest {

    private final LoginController loginController;
    private final UserService userService;
    private final FolderService folderService;
    private final FileService fileService;
    private final JpQueryUtil query;

    private HttpStatus responseEntityStatus;
    private User mockUser;
    private Folder mockFolder;
    private MultipartFile attachedMockFile;

    public FileUploadFeatureTest(
            LoginController loginController,
            UserService userService,
            FolderService folderService,
            FileService fileService,
            JpQueryUtil query) {
        this.loginController = loginController;
        this.userService = userService;
        this.folderService = folderService;
        this.fileService = fileService;
        this.query = query;

    }

    @After("@cleanupUploadData")
    public void cleanUpMockData() {
       query.deleteUser(mockUser.getUsername());
    }

    @Given("the user has an account with username {string} and password {string}")
    public void theUserHasAnAccountWithUsernameAndPassword(String username, String password) {
        ResponseEntity<SuccessDto> responseEntity = userService.registerUser(username, password).toResponseEntity();
        responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        assertEquals(HttpStatus.CREATED, responseEntityStatus);

        // Instantiate registered mock user for re-use and post test deletion
        mockUser = userService.loadUserByUsername(username);

    }

    @Given("the user has a folder with name {string}")
    public void theUserHasAFolderWithName(String folderName) {
        ResponseEntity<SuccessDto> responseEntity = folderService.createFolder(mockUser.getId(), folderName).toResponseEntity();
        responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        assertEquals(HttpStatus.CREATED, responseEntityStatus);

        // Instantiate created folder for re-use
        mockFolder = query.loadFolderByUserAndName(mockUser, folderName);
    }


    @Given("the user is logged in with username {string} and password {string}")
    public void theUserIsLoggedInWithUsernameAndPassword(String username, String password) {
        UserCredentialsDto dto = new UserCredentialsDto(username, password);
        ResponseEntity<SuccessDto> responseEntity = loginController.login(dto);
        responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        assertEquals(HttpStatus.OK, responseEntityStatus);
    }

    @When("the user uploads a file with name {string} into her folder with name {string}")
    public void theUserUploadsAFileWithNameIntoHerFolderWithName(String fileName, String folderName) {
        long userId = mockUser.getId();
        long folderId = mockFolder.getId();
        attachedMockFile = getMockFile(fileName);

        ResponseEntity<SuccessDto> responseEntity = fileService.upload(userId, folderId, attachedMockFile).toResponseEntity();
        responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        assertEquals(HttpStatus.CREATED, responseEntityStatus);
        assertEquals(folderName, mockFolder.getName());

    }

    @Then("the file should be uploaded successfully in the users folder")
    public void theFileShouldBeUploadedSuccessfully() {
        String mockFileName = attachedMockFile.getOriginalFilename();
        File mockFile = query.loadFileByFolderAndName(mockFolder, mockFileName);

        boolean isSuccessfulUpload =
                query.isUserOwnerOfTargetFolder(mockUser, mockFolder.getId()) &&
                        query.isFilePartOfTargetFolder(mockFile.getId(), mockFolder);

        assertTrue(isSuccessfulUpload);
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
