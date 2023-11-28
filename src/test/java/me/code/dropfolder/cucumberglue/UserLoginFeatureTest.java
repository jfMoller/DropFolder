package me.code.dropfolder.cucumberglue;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import me.code.dropfolder.controllers.LoginController;
import me.code.dropfolder.dtos.SuccessDto;
import me.code.dropfolder.dtos.UserCredentialsDto;
import me.code.dropfolder.exceptions.types.CouldNotFindUserException;
import me.code.dropfolder.exceptions.types.InvalidCredentialsException;
import me.code.dropfolder.services.UserService;
import me.code.dropfolder.utils.JpQueryUtil;
import me.code.dropfolder.utils.MockDataFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains Cucumber step definitions for testing the user login feature.
 * It includes scenarios for successful and unsuccessful user logins.
 */
public class UserLoginFeatureTest {

    private final LoginController loginController;
    private final UserService userService;
    private final JpQueryUtil query;
    private final MockDataFactory mock;
    private String mockUsername;

    /**
     * Constructor for initializing the UserLoginFeatureTest class.
     *
     * @param loginController The login controllers used for user login operations.
     * @param userService     The user services responsible for user-related actions.
     * @param query           The JpQueryUtil used for querying user data.
     * @param mock            The MockDataFactory used for creating mock data.
     */
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

    /**
     * Cucumber hook method executed after scenarios tagged with "@cleanupLoginData".
     * Cleans up mock user data created during tests.
     */
    @After("@cleanupLoginData")
    public void cleanUpMockData() {
        query.deleteUser(this.mockUsername);
    }

    /**
     * Cucumber step definition for the scenario where the user has a registered account.
     * Registers a user and asserts successful registration.
     *
     * @param username The username for user registration.
     * @param password The password for user registration.
     */
    @Given("the user has a registered account with username {string} and password {string}")
    public void theUserHasARegisteredAccountWithUsernameAndPassword(String username, String password) {
        ResponseEntity<SuccessDto> registrationResult =
                userService.registerUser(username, password).toResponseEntity();

        assertEquals(HttpStatus.CREATED, registrationResult.getStatusCode());
        assertDoesNotThrow(() -> userService.loadUserByUsername(username));

        saveMockUsernameForDeletion(username);
    }

    /**
     * Saves the provided username for post scenario deletion.
     *
     * @param username The username of the user to be deleted during cleanup.
     */
    private void saveMockUsernameForDeletion(String username) {
        this.mockUsername = username;
    }

    /**
     * Cucumber step definition for the scenario where the user successfully logs in.
     * Logs in with provided credentials and asserts successful login.
     *
     * @param username The username for user login.
     * @param password The password for user login.
     */
    @Then("the user successfully logs in with username {string} and password {string}")
    public void theUserLogsInWithUsernameAndPassword(String username, String password) {
        UserCredentialsDto mockDto = mock.createMockCredentials(username, password);
        ResponseEntity<SuccessDto> loginResult = loginController.login(mockDto);

        assertNotNull(loginResult);
        assertEquals(HttpStatus.OK, loginResult.getStatusCode());
        assertNotNull(loginResult.getBody());
        assertTrue(loginResult.getBody().getSuccess());
    }

    /**
     * Attempts a login with a valid username and an invalid password,
     * expecting an InvalidCredentialsException to be thrown.
     *
     * @param validUsername   The valid username for login attempt.
     * @param invalidPassword The invalid password for login attempt.
     * @throws InvalidCredentialsException Thrown if the login attempt with the provided credentials is invalid.
     */
    @Then("the user can not log in with a valid username {string} and an invalid valid password {string}")
    public void theUserCanNotLogInWithAValidUsernameAndAnInvalidValidPassword(String validUsername, String invalidPassword) {
        UserCredentialsDto mockDto = mock.createMockCredentials(validUsername, invalidPassword);

        assertThrows(InvalidCredentialsException.class, () -> loginController.login(mockDto));
    }

    /**
     * Attempts a login with an invalid username and a valid password,
     * expecting an InvalidCredentialsException to be thrown.
     *
     * @param invalidUsername The invalid username for login attempt.
     * @param validPassword   The valid password for login attempt.
     * @throws InvalidCredentialsException Thrown if the login attempt with the provided credentials is invalid.
     */
    @And("the user can not log in with an invalid username {string} and a valid password {string}")
    public void theUserCanNotLogInWithAnInvalidUsernameAndAValidPassword(String invalidUsername, String validPassword) {
        UserCredentialsDto mockDto = mock.createMockCredentials(invalidUsername, validPassword);

        assertThrows(InvalidCredentialsException.class, () -> loginController.login(mockDto));
    }

    /**
     * Cucumber step definition for the scenario where the user does not have a registered account.
     * Asserts that the user is not registered.
     *
     * @param username The username for checking user registration.
     */
    @Given("the user does not have a registered account with username {string}")
    public void theUserDoesNotHaveARegisteredAccountWithUsername(String username) {
        assertThrows(CouldNotFindUserException.class, () -> userService.loadUserByUsername(username));
    }

    /**
     * Attempts a login with a non-registered user,
     * expecting an InvalidCredentialsException to be thrown.
     *
     * @param username The non-registered username for login attempt.
     * @param password The non-registered password for login attempt.
     * @throws InvalidCredentialsException Thrown if the login attempt with the provided credentials is invalid.
     */
    @Then("the user can not log in with a non-registered username {string} and a non-registered password {string}")
    public void theUserCanNotLogInWithANonRegisteredUsernameAndANonRegisteredPassword(String username, String password) {
        UserCredentialsDto mockDto = mock.createMockCredentials(username, password);

        assertThrows(InvalidCredentialsException.class, () -> loginController.login(mockDto));
    }

}
