package me.code.dropfolder.cucumberglue;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import me.code.dropfolder.dtos.SuccessDto;
import me.code.dropfolder.dtos.UserCredentialsDto;
import me.code.dropfolder.exceptions.GlobalExceptionHandler;
import me.code.dropfolder.exceptions.dtos.ErrorDto;
import me.code.dropfolder.exceptions.types.CouldNotFindUserException;
import me.code.dropfolder.exceptions.types.PasswordValidationException;
import me.code.dropfolder.exceptions.types.UsernameValidationException;
import me.code.dropfolder.services.UserRegistrationValidator;
import me.code.dropfolder.services.UserService;
import me.code.dropfolder.utils.JpQueryUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cucumber test class for user registration scenarios.
 * This class covers both successful and unsuccessful user registration scenarios.
 */
public class UserRegistrationFeatureTest {

    private final GlobalExceptionHandler globalExceptionHandler;
    private final UserService userService;
    private final UserRegistrationValidator userRegistrationValidator;
    private final JpQueryUtil query;
    private UserCredentialsDto mockCredentials;
    private HttpStatus responseEntityStatus;

    /**
     * A constant used to set mock user credentials as null.
     */
    private static final String SET_AS_NULL_FLAG = "set_as_null";

    /**
     * Constructs a new instance of the {@code UserRegistrationFeatureTest} class.
     *
     * @param globalExceptionHandler    An instance of {@code GlobalExceptionHandler} for handling exceptions.
     * @param userService               An instance of {@code UserService} for user registration.
     * @param userRegistrationValidator An instance of {@code UserRegistrationValidator} for validating user credentials.
     * @param query                     An instance of {@code JpQueryUtil} for executing database queries.
     */
    public UserRegistrationFeatureTest(
            GlobalExceptionHandler globalExceptionHandler,
            UserService userService,
            UserRegistrationValidator userRegistrationValidator,
            JpQueryUtil query) {
        this.globalExceptionHandler = globalExceptionHandler;
        this.userService = userService;
        this.userRegistrationValidator = userRegistrationValidator;
        this.query = query;
    }

    /**
     * Cleans up mock user data after the execution of scenarios tagged with "@cleanupRegistrationData".
     */
    @After("@cleanupRegistrationData")
    public void cleanUpMockData() {
        query.deleteUser(mockCredentials.username());
    }

    /**
     * Step definition for providing valid registration details.
     *
     * @param username The username for registration.
     * @param password The password for registration.
     */
    @Given("the user provides valid registration credentials with username {string} and password {string}")
    public void theUserProvidesValidRegistrationDetails(String username, String password) {
        mockCredentials = new UserCredentialsDto(username, password);

        boolean hasUsernameFormattingError =
                userRegistrationValidator
                        .hasUsernameFormattingError(mockCredentials.username());

        boolean hasPasswordFormattingError =
                userRegistrationValidator
                        .hasPasswordFormattingError(mockCredentials.password());

        assertNotNull(mockCredentials);
        assertFalse(hasUsernameFormattingError);
        assertFalse(hasPasswordFormattingError);
    }

    /**
     * Step definition for submitting the registration.
     * Handles exceptions related to username and password formatting.
     */
    @When("the user submits the registration")
    public void theUserSubmitsTheRegistration() {
        try {
            ResponseEntity<SuccessDto> responseEntity =
                    userService.registerUser(mockCredentials.username(), mockCredentials.password())
                            .toResponseEntity();

            assertNotNull(responseEntity.getStatusCode());
            assertNotNull(responseEntity.getBody());
            assertTrue(responseEntity.getBody().getSuccess());

            responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        } catch (UsernameValidationException | PasswordValidationException exception) {
            ResponseEntity<ErrorDto> responseEntity =
                    globalExceptionHandler.handleValidationException(exception);

            assertNotNull(responseEntity.getStatusCode());
            assertNotNull(responseEntity.getBody());
            assertTrue(responseEntity.getBody().getError());

            responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();
        }
    }

    /**
     * Step definition for asserting successful user registration.
     */
    @Then("the user should be registered successfully")
    public void theUserShouldBeRegisteredSuccessfully() {
        assertEquals(HttpStatus.CREATED, responseEntityStatus);
        assertDoesNotThrow(() -> userService.loadUserByUsername(mockCredentials.username()));
    }

    /**
     * Step definition for providing invalid registration details.
     *
     * @param username The invalid username for registration.
     * @param password The invalid password for registration.
     */
    @Given("the user provides invalid registration credentials with username {string} and password {string}")
    public void theUserProvidesInvalidRegistrationDetailsWithUsernameAndPassword(String username, String password) {
        username = setAsNullIfFlagged(username);
        password = setAsNullIfFlagged(password);

        mockCredentials = new UserCredentialsDto(username, password);
        assertNotNull(mockCredentials);

        boolean hasUsernameFormattingError =
                userRegistrationValidator
                        .hasUsernameFormattingError(mockCredentials.username());

        boolean hasPasswordFormattingError =
                userRegistrationValidator
                        .hasPasswordFormattingError(mockCredentials.password());

        assertTrue(hasUsernameFormattingError || hasPasswordFormattingError);
    }

    /**
     * Converts a specified string value to null if it is flagged as "set_as_null".
     * If the input string is not equal to "set_as_null", it returns the input string.
     *
     * @param string The input string to be checked for the "set_as_null" flag.
     * @return Null if the input string is "set_as_null", otherwise returns the input string.
     */
    private String setAsNullIfFlagged(String string) {
        if (string.equals(SET_AS_NULL_FLAG)) {
            return null;
        } else return string;
    }

    /**
     * Step definition for asserting failed user registration.
     */
    @Then("the registration should fail")
    public void theRegistrationShouldFail() {
        assertNotEquals(HttpStatus.CREATED, responseEntityStatus);
        assertThrows(CouldNotFindUserException.class,
                () -> userService.loadUserByUsername(mockCredentials.username()));
    }
}
