package me.code.dropfolder.cucumberglue;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import me.code.dropfolder.util.MockDataFactory;
import me.code.dropfolder.dto.SuccessDto;
import me.code.dropfolder.model.File;
import me.code.dropfolder.model.Folder;
import me.code.dropfolder.model.User;
import me.code.dropfolder.service.file.FileService;
import me.code.dropfolder.util.JpQueryUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cucumber feature test class for the file upload scenario.
 * This class defines step definitions using Cucumber annotations to test
 * the file upload functionality with mock data.
 */
public class FileUploadFeatureTest {

    private final FileService fileService;
    private final JpQueryUtil query;
    private final MockDataFactory mock;

    private User mockUser;
    private Folder mockFolder;
    private MultipartFile attachedMockFile;

    /**
     * Constructor for the FileUploadFeatureTest class.
     *
     * @param fileService The file service used for file-related operations.
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
        mockUser = mock.createMockUser();
    }

    /**
     * Cleanup method annotated with {@code @After} to delete the mock user data after the test scenario.
     * Note: Due to cascade relationships in the database, deleting a user will also delete all of that user's folders and files.
     */
    @After("@cleanupUploadData")
    public void cleanUpMockData() {
        query.deleteUser(mockUser.getUsername());
    }

    /**
     * Cucumber step definition for the scenario where the user has a folder with a given name.
     *
     * @param folderName The name of the folder.
     */
    @Given("the user has a folder with name {string}")
    public void theUserHasAFolderWithName(String folderName) {
        mockFolder = mock.createMockFolder(mockUser, folderName);

        assertEquals(folderName, mockFolder.getName());
        assertTrue(query.userHasExistingFolderByName(mockUser, folderName));
    }

    /**
     * Cucumber step definition for the scenario where the user uploads a file into their folder.
     *
     * @param fileName The name of the file to be uploaded.
     */
    @When("the user uploads a file with name {string} into their folder")
    public void theUserUploadsAFileWithNameIntoHerFolderWithName(String fileName) {
        long userId = mockUser.getId();
        long folderId = mockFolder.getId();
        attachedMockFile = mock.getMockFile(fileName);

        ResponseEntity<SuccessDto> responseEntity =
                fileService.upload(userId, folderId, attachedMockFile).toResponseEntity();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    /**
     * Cucumber step definition for the scenario where the uploaded file should be in the user's folder.
     *
     * @see Then
     */
    @Then("the file should be uploaded successfully in the users folder")
    public void theFileShouldBeUploadedSuccessfully() {
        String mockFileName = attachedMockFile.getOriginalFilename();
        File mockFile = query.loadFileByFolderAndName(mockFolder, mockFileName);

        boolean isSuccessfulUpload =
                query.isUserOwnerOfTargetFolder(mockUser, mockFolder.getId()) &&
                        query.isFilePartOfTargetFolder(mockFile.getId(), mockFolder);

        assertTrue(isSuccessfulUpload);
    }

}
