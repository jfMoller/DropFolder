package me.code.dropfolder.cucumberglue;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import me.code.dropfolder.dtos.SuccessDto;
import me.code.dropfolder.exceptions.types.CouldNotFindFolderException;
import me.code.dropfolder.exceptions.types.FileUploadFailureException;
import me.code.dropfolder.models.File;
import me.code.dropfolder.models.Folder;
import me.code.dropfolder.models.User;
import me.code.dropfolder.services.FileService;
import me.code.dropfolder.utils.JpQueryUtil;
import me.code.dropfolder.utils.MockDataFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cucumber feature test class for the file upload scenario. This class defines step definitions
 * using Cucumber annotations to test the file upload functionality with mock data.
 *
 * <p>
 * The class sets up and cleans up mock user data before and after test scenarios, and it defines
 * step definitions for various successful and unsuccessful file upload scenarios.
 * </p>
 */
public class FileUploadFeatureTest {

    private final FileService fileService;
    private final JpQueryUtil query;
    private final MockDataFactory mock;


    private User primaryMockUser;
    private Folder primaryMockUsersFolder;
    private User secondaryMockUser;
    private MultipartFile attachedMockFile;

    /**
     * Constructor for the FileUploadFeatureTest class.
     *
     * @param fileService The file services used for file-related operations.
     * @param query       The JpQueryUtil used for querying data.
     * @param mock        The MockDataFactory used for creating mock data.
     */
    public FileUploadFeatureTest(
            FileService fileService,
            JpQueryUtil query,
            MockDataFactory mock) {
        this.fileService = fileService;
        this.query = query;
        this.mock = mock;
    }

    /**
     * Setup method annotated with {@code @Before} to create mock user data before the test scenario.
     */
    @Before("@setupUploadData")
    public void setUpMockData() {
        primaryMockUser = mock.createMockUser("Mockuser1", "mockPassword2");
        secondaryMockUser = mock.createMockUser("MockUser2", "mockPassword2");
    }

    /**
     * Cleanup method annotated with {@code @After} to delete the mock user data after the test scenario.
     * Note: Due to entity relationship settings, deleting a user will also delete all of that user's folders and files.
     */
    @After("@cleanupUploadData")
    public void cleanUpMockData() {
        query.deleteUser(primaryMockUser.getUsername());
        query.deleteUser(secondaryMockUser.getUsername());
    }

    /**
     * Step definition for the scenario where the user has a folder with a given name.
     *
     * @param folderName The name of the folder.
     */
    @Given("the user has a folder with name {string}")
    public void theUserHasAFolderWithName(String folderName) {
        primaryMockUsersFolder = mock.createMockFolder(primaryMockUser, folderName);

        assertEquals(folderName, primaryMockUsersFolder.getName());
        assertTrue(query.userHasExistingFolderByName(primaryMockUser, folderName));
    }

    /**
     * Step definition for the scenario where the user uploads a file into their folder.
     *
     * @param fileName The name of the file to be uploaded.
     */
    @When("the user uploads a file with name {string} into their folder")
    public void theUserUploadsAFileWithNameIntoHerFolderWithName(String fileName) {
        long userId = primaryMockUser.getId();
        long folderId = primaryMockUsersFolder.getId();
        attachedMockFile = mock.generateMockFile(fileName);

        ResponseEntity<SuccessDto> responseEntity =
                fileService.upload(userId, folderId, attachedMockFile).toResponseEntity();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    /**
     * Step definition for the scenario where the uploaded file should be in the user's folder.
     */
    @Then("the file should be uploaded successfully in the users folder")
    public void theFileShouldBeUploadedSuccessfully() {
        String mockFileName = attachedMockFile.getOriginalFilename();
        File mockFile = query.loadFileByFolderAndName(primaryMockUsersFolder, mockFileName);

        boolean isSuccessfulUpload =
                query.isUserOwnerOfTargetFolder(primaryMockUser, primaryMockUsersFolder.getId()) &&
                        query.isFilePartOfTargetFolder(mockFile.getId(), primaryMockUsersFolder);

        assertTrue(isSuccessfulUpload);
    }

    /**
     * Step definition for the scenario where the user does not own a folder with a given name.
     *
     * @param folderName The name of the folder that the user does not own.
     */
    @Given("the user does not own a folder with name {string}")
    public void theUserDoesNotOwnAFolderWithName(String folderName) {
        primaryMockUsersFolder = mock.createMockFolder(primaryMockUser, folderName);

        assertEquals(folderName, primaryMockUsersFolder.getName());
        assertFalse(query.userHasExistingFolderByName(secondaryMockUser, folderName));
    }

    /**
     * Step definition for the scenario where the upload should fail if a user tries to upload
     * a file into a folder that they do not own.
     *
     * @param fileName The name of the file to be uploaded by a non-owner user into a folder owned by another user.
     */
    @Then("the upload should fail if a user tries to upload a file with name {string} into a folder that they do not own")
    public void theUploadShouldFailIfAUserTriesToUploadAFileWithNameIntoAFolderThatTheyDoNotOwn(String fileName) {
        long nonOwnerUserId = secondaryMockUser.getId();
        long notOwnedFolderId = primaryMockUsersFolder.getId();
        attachedMockFile = mock.generateMockFile(fileName);

        assertThrows(FileUploadFailureException.class,
                () -> fileService.upload(nonOwnerUserId, notOwnedFolderId, attachedMockFile));
    }

    /**
     * Step definition for the scenario where there is no existing folder with a specified ID.
     *
     * @param id The ID of the non-existing folder.
     */
    @Given("there is no existing folder with id {string}")
    public void thereIsNoExistingFolderWithName(String id) {
        long nonExistingId = Long.parseLong(id);
        assertThrows(CouldNotFindFolderException.class, () -> query.loadFolderById(nonExistingId));
    }

    /**
     * Step definition for the scenario where the upload should fail if a user tries to upload
     * a file into a non-existing folder with a specified ID.
     *
     * @param fileName The name of the file to be uploaded by a user.
     * @param id       The ID of the non-existing folder into which the user attempts to upload the file.
     */
    @Then("the upload should fail if a user tries to upload a file with name {string} into a non-existing folder with id {string}")
    public void theUploadShouldFailIfAUserTriesToUploadAFileWithNameIntoANonExistingFolderWithId(String fileName, String id) {
        long userId = secondaryMockUser.getId();
        long nonExistingFolderId = Long.parseLong(id);
        attachedMockFile = mock.generateMockFile(fileName);

        assertThrows(FileUploadFailureException.class,
                () -> fileService.upload(userId, nonExistingFolderId, attachedMockFile));
    }

}
