package me.code.dropfolder.cucumberglue;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import me.code.dropfolder.controller.LoginController;
import me.code.dropfolder.dto.SuccessDto;
import me.code.dropfolder.dto.UserCredentialsDto;
import me.code.dropfolder.service.user.UserService;
import me.code.dropfolder.util.JpQueryUtil;
import me.code.dropfolder.util.MockDataFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class UserLoginFeatureTest {

    private final LoginController loginController;
    private final UserService userService;
    private final JpQueryUtil query;
    private final MockDataFactory mock;
    private String mockUsername;

    public UserLoginFeatureTest(
            LoginController loginController,
            UserService userService,
            JpQueryUtil query,
            MockDataFactory mock) {
        this.loginController = loginController;
        this.userService = userService;
        this.query = query;
        this.mock = mock;
    }

    @After("@cleanupLoginData")
    public void cleanUpMockData() {
        query.deleteUser(this.mockUsername);
    }

    @Given("the user has a registered account with username {string} and password {string}")
    public void theUserHasARegisteredAccountWithUsernameAndPassword(String username, String password) {
        ResponseEntity<SuccessDto> registrationResult =
                userService.registerUser(username, password).toResponseEntity();

        assertEquals(HttpStatus.CREATED, registrationResult.getStatusCode());
        assertDoesNotThrow(() -> userService.loadUserByUsername(username));

        saveMockUsernameForDeletion(username);
    }

    @Then("the user successfully logs in with username {string} and password {string}")
    public void theUserLogsInWithUsernameAndPassword(String username, String password) {
        UserCredentialsDto mockDto = mock.createMockCredentials(username, password);
        ResponseEntity<SuccessDto> loginResult = loginController.login(mockDto);

        assertNotNull(loginResult);
        assertEquals(HttpStatus.OK, loginResult.getStatusCode());
        assertNotNull(loginResult.getBody());
        assertTrue(loginResult.getBody().getSuccess());
    }

    private void saveMockUsernameForDeletion(String username) {
        this.mockUsername = username;
    }
}
