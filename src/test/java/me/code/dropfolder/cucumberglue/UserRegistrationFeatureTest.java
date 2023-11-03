package me.code.dropfolder.cucumberglue;

import me.code.dropfolder.dto.Success;
import me.code.dropfolder.dto.UserCredentials;
import me.code.dropfolder.exception.ExceptionHandler;
import me.code.dropfolder.exception.dto.Error;
import me.code.dropfolder.exception.type.PasswordFormattingException;
import me.code.dropfolder.exception.type.UsernameFormattingException;
import me.code.dropfolder.service.user.UserRegistrationValidator;
import me.code.dropfolder.service.user.UserService;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class contains step definitions for user registration scenarios in Cucumber tests.
 */
public class UserRegistrationFeatureTest {

    private final ExceptionHandler exceptionHandler;
    private final UserService userService;
    private final UserRegistrationValidator userRegistrationValidator;
    private UserCredentials userCredentials;
    private HttpStatus responseEntityStatus;

    /**
     * Constructor for UserRegistrationFeatureTest class.
     *
     * @param exceptionHandler          handles for handling generic and specific runtime exceptions.
     * @param userService               handles user registration.
     * @param userRegistrationValidator handles validation of user credentials.
     */
    public UserRegistrationFeatureTest(ExceptionHandler exceptionHandler, UserService userService, UserRegistrationValidator userRegistrationValidator) {
        this.exceptionHandler = exceptionHandler;
        this.userService = userService;
        this.userRegistrationValidator = userRegistrationValidator;
    }

    /**
     * Clean up method to delete mock user data after a scenario.
     */
    @After
    public void cleanUpMockData() {
        userService.deleteUser(userCredentials.username());
    }

    /**
     * Step definition for providing valid registration details.
     *
     * @param username The username for registration.
     * @param password The password for registration.
     */
    @Given("a user provides valid registration credentials with username {string} and password {string}")
    public void aUserProvidesValidRegistrationDetails(String username, String password) {
        userCredentials = new UserCredentials(username, password);

        Assertions.assertNotNull(userCredentials);
        Assertions.assertFalse(userRegistrationValidator.hasUsernameFormattingError(userCredentials.username()));
        Assertions.assertFalse(userRegistrationValidator.hasPasswordFormattingError(userCredentials.password()));
    }

    /**
     * Step definition for submitting the registration form.
     * It also handles exceptions related to username and password formatting.
     */
    @When("the user submits the registration")
    public void theUserSubmitsTheRegistrationForm() {
        try {
            ResponseEntity<Success> responseEntity =
                    userService.registerUser(userCredentials.username(), userCredentials.password());
            responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

            Assertions.assertNotNull(responseEntityStatus);

        } catch (UsernameFormattingException exception) {
            // Handles username formatting exception
            ResponseEntity<Error> responseEntity = exceptionHandler.handleUsernameFormattingException(exception);
            responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        } catch (PasswordFormattingException exception) {
            // Handles password formatting exception
            ResponseEntity<Error> responseEntity = exceptionHandler.handlePasswordFormattingException(exception);
            responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();
        }
    }

    @Then("the user should be registered successfully")
    public void theUserShouldBeRegisteredSuccessfully() {
        assertEquals(HttpStatus.CREATED, responseEntityStatus);
    }

    /**
     * Step definition for providing invalid registration details.
     *
     * @param username The invalid username for registration.
     * @param password The invalid password for registration.
     */
    @Given("a user provides invalid registration credentials with username {string} and password {string}")
    public void aUserProvidesInvalidRegistrationDetailsWithUsernameAndPassword(String username, String password) {
        userCredentials = new UserCredentials(username, password);
        Assertions.assertNotNull(userCredentials);

        Assertions.assertTrue(
                userRegistrationValidator.hasUsernameFormattingError(userCredentials.username()) ||
                        userRegistrationValidator.hasPasswordFormattingError(userCredentials.password()));
    }

    @Then("the registration should fail")
    public void theRegistrationShouldFail() {
        assertEquals(HttpStatus.BAD_REQUEST, responseEntityStatus);
    }
}
