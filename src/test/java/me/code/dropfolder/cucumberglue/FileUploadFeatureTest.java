package me.code.dropfolder.cucumberglue;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import me.code.dropfolder.controller.LoginController;
import me.code.dropfolder.dto.Success;
import me.code.dropfolder.dto.UserCredentials;
import me.code.dropfolder.model.File;
import me.code.dropfolder.service.file.FileService;
import me.code.dropfolder.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileUploadFeatureTest {

    private final LoginController loginController;
    private final UserService userService;
    private final FileService fileService;

    private HttpStatus responseEntityStatus;
    private String mockUsername;
    private MultipartFile attachedMockFile;

    public FileUploadFeatureTest(LoginController loginController, UserService userService, FileService fileService) {
        this.loginController = loginController;
        this.userService = userService;
        this.fileService = fileService;
    }

    @After("@cleanupUploadData")
    public void cleanUpMockData() {
        userService.deleteUser(mockUsername);
        fileService.deleteFile(attachedMockFile.getOriginalFilename());
    }

    @Given("the user has an account with username {string} and password {string}")
    public void theUserHasAnAccountWithUsernameAndPassword(String username, String password) {
        ResponseEntity<Success> responseEntity = userService.registerUser(username, password).toResponseEntity();
        responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        assertEquals(HttpStatus.CREATED, responseEntityStatus);

        mockUsername = username;
    }


    @Given("the user is logged in with username {string} and password {string}")
    public void theUserIsLoggedInWithUsernameAndPassword(String username, String password) {
        UserCredentials dto = new UserCredentials(username, password);
        ResponseEntity<Success> responseEntity = loginController.login(dto);
        responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        assertEquals(HttpStatus.OK, responseEntityStatus);
    }

    @When("the user uploads a file with name {string}")
    public void theUserUploadsAFileWithName(String name) {
        attachedMockFile = getMockFile(name);

        ResponseEntity<Success> responseEntity = fileService.upload(attachedMockFile).toResponseEntity();
        ;
        responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        assertEquals(HttpStatus.CREATED, responseEntityStatus);

    }

    @Then("the file should be uploaded successfully")
    public void theFileShouldBeUploadedSuccessfully() {
        String mockFileName = attachedMockFile.getOriginalFilename();
        File file = fileService.getFile(mockFileName);

        assertNotNull(file);
        assertEquals(mockFileName, file.getName());
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
