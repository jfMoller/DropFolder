package me.code.dropfolder.cucumberglue;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import me.code.dropfolder.dto.SuccessDto;
import me.code.dropfolder.exception.type.CouldNotFindFileException;
import me.code.dropfolder.exception.type.FileDeletionFailureException;
import me.code.dropfolder.model.File;
import me.code.dropfolder.model.Folder;
import me.code.dropfolder.model.User;
import me.code.dropfolder.service.file.FileService;
import me.code.dropfolder.util.JpQueryUtil;
import me.code.dropfolder.util.MockDataFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cucumber feature test class for file deletion scenarios.
 * Defines step definitions using Cucumber annotations to test the file deletion functionality with mock data.
 *
 * <p>
 * The class sets up and cleans up mock user data before and after test scenarios and defines step definitions
 * for various successful and unsuccessful file deletion scenarios.
 * </p>
 */
public class FileDeletionFeatureTest {

    private final FileService fileService;
    private final JpQueryUtil query;
    private final MockDataFactory mock;

    private User primaryMockUser;
    private Folder primaryMockFolder;
    private User secondaryMockUser;
    private Folder secondaryMockFolder;
    private File mockFile;

    /**
     * Constructor for the FileDeletionFeatureTest class.
     *
     * @param fileService The file service used for file-related operations.
     * @param query       The JpQueryUtil used for querying data.
     * @param mock        The MockDataFactory used for creating mock data.
     */
    public FileDeletionFeatureTest(
            FileService fileService,
            JpQueryUtil query,
            MockDataFactory mock) {
        this.fileService = fileService;
        this.query = query;
        this.mock = mock;
    }

    /**
     * Setup method annotated with {@code @Before} to create mock data before the test scenario.
     */
    @Before("@setupDeletionData")
    public void setupMockData() {
        primaryMockUser = mock.createMockUser();
        secondaryMockUser = mock.createMockUser("mockUser2", "Mockpassword");
        primaryMockFolder = mock.createMockFolder(primaryMockUser, "mock_folder");
    }

    /**
     * Cleanup method annotated with {@code @After} to delete the mock data after the test scenario.
     * Note: Due to cascade relationships in the database, deleting a user will also delete all of that user's folders and files.
     */
    @After("@cleanupDeletionData")
    public void cleanupMockData() {
        query.deleteUser(primaryMockUser.getUsername());
        query.deleteUser(secondaryMockUser.getUsername());
    }

    /**
     * Step definition for the scenario where the user owns a folder containing a file with a given name.
     *
     * @param fileName The name of the file in the user's folder.
     */
    @Given("the user owns a folder containing a file named {string}")
    public void theUserOwnsAFolderContainingAFileNamed(String fileName) {
        long userId = primaryMockUser.getId();
        long folderId = primaryMockFolder.getId();
        MultipartFile attachedMockFile = mock.getMockFile(fileName);

        ResponseEntity<SuccessDto> responseEntity =
                fileService.upload(userId, folderId, attachedMockFile).toResponseEntity();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(query.isUserOwnerOfTargetFolder(primaryMockUser, primaryMockFolder.getId()));
        assertTrue(query.folderHasExistingFileByName(primaryMockFolder, fileName));

    }

    /**
     * Step definition for the scenario where the user deletes a file with a given name.
     *
     * @param fileName The name of the file to be deleted.
     */
    @When("the user deletes the file with name {string}")
    public void theUserDeletesTheFileWithName(String fileName) {
        mockFile = query.loadFileByFolderAndName(primaryMockFolder, fileName);

        query.deleteFile(fileName);

        assertThrows(CouldNotFindFileException.class,
                () -> query.loadFileByFolderAndName(primaryMockFolder, fileName));
    }

    /**
     * Step definition for the scenario where the file should be successfully deleted from the user's folder.
     */
    @Then("the file should be successfully deleted from the user's folder")
    public void theFileShouldBeSuccessfullyDeletedFromTheUsersFolder() {
        long mockFileId = mockFile.getId();

        assertFalse(query.isFilePartOfTargetFolder(mockFileId, primaryMockFolder));
    }

    /**
     * Step definition for the scenario where the user does not own a folder with a given name.
     *
     * @param folderName The name of the folder that the user does not own.
     */
    @Given("the user does not own a folder named {string}")
    public void theUserDoesNotOwnAFolderNamed(String folderName) {
        primaryMockFolder = mock.createMockFolder(primaryMockUser, folderName);

        assertFalse(query.userHasExistingFolderByName(secondaryMockUser, folderName));
    }

    /**
     * Step definition for the scenario where the user does not own an uploaded file in the folder.
     *
     * @param fileName The name of the file that the user does not own.
     */
    @Then("the user does not own an uploaded file named {string} in the folder")
    public void theUserDoesNotOwnAnUploadedFileNamedInTheFolderWithName(String fileName) {
        long otherUsersId = primaryMockUser.getId();
        long otherUsersFolderId = primaryMockFolder.getId();

        File fileOwnedByOtherUser =
                mock.createMockFile(otherUsersId, otherUsersFolderId, fileName);

        assertTrue(query.userIsNotOwnerOfTargetFolder(secondaryMockUser, primaryMockFolder));
        assertTrue(query.userIsNotOwnerOfTargetFile(secondaryMockUser, primaryMockFolder, fileOwnedByOtherUser));
    }

    /**
     * Step definition for the scenario where the deletion should fail if the user tries to delete
     * a file in a folder that they do not own.
     *
     * @param fileName The name of the file to be deleted by a non-owner user in a folder owned by another user.
     */
    @Then("the deletion should fail if the user tries to delete the file named {string} in the folder that they do not own")
    public void theDeletionShouldFailIfAUserTriesToDeleteAFileWithName(String fileName) {
        long userId = secondaryMockUser.getId();
        long otherUsersFolderId = primaryMockFolder.getId();
        long otherUsersFileId = query.getFileId(fileName);

        assertThrows(FileDeletionFailureException.class,
                () -> fileService.delete(userId, otherUsersFolderId, otherUsersFileId));
    }

    /**
     * Step definition for the scenario where the user owns a folder with a given name.
     *
     * @param folderName The name of the folder that the user owns.
     */
    @Given("the user owns a folder named {string}")
    public void theUserOwnsAFolderNamed(String folderName) {
        secondaryMockFolder = mock.createMockFolder(secondaryMockUser, folderName);

        assertTrue(query.isUserOwnerOfTargetFolder(secondaryMockUser, secondaryMockFolder.getId()));
    }

    /**
     * Step definition for the scenario where the user does not have a file with a specified ID in the folder.
     *
     * @param id The ID of the non-existing file.
     */
    @Then("the user does not have a file with id {string} in the folder")
    public void theUserDoesNotHaveAFileNamedInTheFolder(String id) {
        long nonExistingFileId = Long.parseLong(id);

        assertFalse(query.isFilePartOfTargetFolder(nonExistingFileId, secondaryMockFolder));
    }

    /**
     * Step definition for the scenario where the deletion should fail if the user tries to delete
     * a file with a specified ID in the folder that they own.
     *
     * @param id The ID of the non-existing file to be deleted by the user in their folder.
     */
    @Then("the deletion should fail if the user tries to delete the file with id {string} in the folder that they own")
    public void theDeletionShouldFailIfTheUserTriesToDeleteTheFileNamedInTheFolderThatTheyOwn(String id) {
        long userId = secondaryMockUser.getId();
        long usersFolderId = secondaryMockFolder.getId();
        long nonExistingFileId = Long.parseLong(id);

        assertThrows(FileDeletionFailureException.class,
                () -> fileService.delete(userId, usersFolderId, nonExistingFileId));
    }
}
