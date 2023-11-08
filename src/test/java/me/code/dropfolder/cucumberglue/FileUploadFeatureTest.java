package me.code.dropfolder.cucumberglue;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import me.code.dropfolder.dto.Success;
import me.code.dropfolder.exception.ExceptionHandler;
import me.code.dropfolder.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileUploadFeatureTest {

    private final ExceptionHandler exceptionHandler;
    private final UserService userService;
    private final UserService fileService;

    private HttpStatus responseEntityStatus;
    private String mockUsername;
    private MultipartFile attachedMockFile;

    public FileUploadFeatureTest(ExceptionHandler exceptionHandler, UserService userService, FileService fileService) {
        this.exceptionHandler = exceptionHandler;
        this.userService = userService;
        this.fileService = fileService;
    }

    @After("@cleanupUploadData")
    public void cleanUpMockData() {
        userService.deleteUser(mockUsername);
    }

    @Given("the user has an account with username {string} and password {string}")
    public void theUserHasAnAccountWithUsernameAndPassword(String username, String password) {
        ResponseEntity<Success> responseEntity = userService.registerUser(username, password);
        responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        assertEquals(HttpStatus.CREATED, responseEntityStatus);

        mockUsername = username;
    }


    @Given("the user is logged in with username {string} and password {string}")
    public void theUserIsLoggedInWithUsernameAndPassword(String username, String password) {
        ResponseEntity<Success> responseEntity = userService.login(username, password);
        responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        assertEquals(HttpStatus.OK, responseEntityStatus);
    }

    @When("the user uploads a file with name {string}")
    public void theUserUploadsAFileWithName(String name) {
        attachedMockFile = generateMockFileFromName(name);

        ResponseEntity<Success> responseEntity = fileService.upload(attachedMockFile);
        ;
        responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        assertEquals(HttpStatus.CREATED, responseEntityStatus);

    }

    @Then("the file should be uploaded successfully")
    public void theFileShouldBeUploadedSuccessfully() {
        String mockFileName = attachedMockFile.getOriginalFilename();
        MultipartFile file = fileService.getFile(mockFileName);

        assertNotNull(file);
        assertEquals(mockFileName, file.getOriginalFilename());
    }

    private MultipartFile generateMockFileFromName(String name) {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn(name);

        return mockFile;
    }


}
