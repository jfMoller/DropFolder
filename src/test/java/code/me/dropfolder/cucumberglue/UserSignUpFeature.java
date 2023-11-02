package code.me.dropfolder.cucumberglue;

import code.me.dropfolder.dto.Success;
import code.me.dropfolder.dto.UserCredentials;
import code.me.dropfolder.exception.type.RegistrationFormattingException;
import code.me.dropfolder.service.UserService;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserSignUpFeature {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserService userService;

    private UserCredentials userCredentials;

    private HttpStatus responseEntityStatus;

    @After
    public void cleanUpMockData() {
        userService.deleteUser(userCredentials.username());
    }

    @Given("a user provides valid registration details with username {string} and password {string}")
    public void aUserProvidesValidRegistrationDetails(String username, String password) {
        userCredentials =
                new UserCredentials(
                        username, password);

        Assertions.assertNotNull(userCredentials);
        Assertions.assertFalse(userService.hasFormattingErrors(userCredentials.username(), userCredentials.password()));
    }

    @When("the user submits the registration form")
    public void theUserSubmitsTheRegistrationForm() {
        try {
            ResponseEntity<Success> responseEntity = userService.signUp(userCredentials.username(), userCredentials.password());
            responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

            Assertions.assertNotNull(responseEntityStatus);
        } catch (RegistrationFormattingException ex) {
            responseEntityStatus = HttpStatus.BAD_REQUEST;
        }
    }

    @Then("the user should be registered successfully")
    public void theUserShouldBeRegisteredSuccessfully() {
        assertEquals(HttpStatus.OK, responseEntityStatus);
    }

    @Given("a user provides invalid registration details with username {string} and password {string}")
    public void aUserProvidesInvalidRegistrationDetailsWithUsernameAndPassword(String username, String password) {
        userCredentials =
                new UserCredentials(
                        username, password);
        Assertions.assertNotNull(userCredentials);
        Assertions.assertTrue(userService.hasFormattingErrors(userCredentials.username(), userCredentials.password()));
    }

    @Then("the registration should fail")
    public void theRegistrationShouldFail() {
        assertEquals(HttpStatus.BAD_REQUEST, responseEntityStatus);
    }
}
