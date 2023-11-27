package me.code.dropfolder.cucumberglue;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import me.code.dropfolder.dto.SuccessDto;
import me.code.dropfolder.exception.type.CouldNotFindFileException;
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

public class FileDeletionFeatureTest {

    private final FileService fileService;
    private final JpQueryUtil query;
    private final MockDataFactory mock;

    private User mockUser;
    private Folder mockFolder;
    private File mockFile;

    public FileDeletionFeatureTest(
            FileService fileService,
            JpQueryUtil query,
            MockDataFactory mock) {
        this.fileService = fileService;
        this.query = query;
        this.mock = mock;

    }

    @Before("@setupDeletionData")
    public void setupMockData() {
        mockUser = mock.createMockUser();
        mockFolder = mock.createMockFolder(mockUser, "mock_folder");
    }

    @After("@cleanupDeletionData")
    public void cleanupMockData() {
        query.deleteUser(mockUser.getUsername());
    }

    @Given("the user owns a folder containing a file named {string}")
    public void theUserOwnsAFolderContainingAFileNamed(String fileName) {
        long userId = mockUser.getId();
        long folderId = mockFolder.getId();
        MultipartFile attachedMockFile = mock.getMockFile(fileName);

        ResponseEntity<SuccessDto> responseEntity =
                fileService.upload(userId, folderId, attachedMockFile).toResponseEntity();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(query.isUserOwnerOfTargetFolder(mockUser, mockFolder.getId()));
        assertTrue(query.folderHasExistingFileByName(mockFolder, fileName));

    }

    @When("the user deletes the file with name {string}")
    public void theUserDeletesTheFileWithName(String fileName) {
        mockFile = query.loadFileByFolderAndName(mockFolder, fileName);

        query.deleteFile(fileName);

        assertThrows(CouldNotFindFileException.class,
                () -> query.loadFileByFolderAndName(mockFolder, fileName));
    }

    @Then("the file should be successfully deleted from the user's folder")
    public void theFileShouldBeSuccessfullyDeletedFromTheUsersFolder() {
        long mockFileId = mockFile.getId();

        assertFalse(query.isFilePartOfTargetFolder(mockFileId, mockFolder));
    }

}
