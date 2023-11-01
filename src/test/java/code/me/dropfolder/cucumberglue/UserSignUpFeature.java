package code.me.dropfolder.cucumberglue;

import code.me.dropfolder.dto.Success;
import code.me.dropfolder.dto.UserCredentials;
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

    @Autowired
    private UserService userService;
    private UserCredentials validUserCredentials;

    private HttpStatus responseEntityStatus;

    private final static String MOCK_USERNAME = "keyboardcat-fnfwe3984bqd333";
    private final static String MOCK_PASSWORD = "keyboardcat-82742HFWEKJeknl";

    @After
    public void cleanUpMockData() {
        userService.deleteUser(MOCK_USERNAME);
    }

    @Given("a user provides valid registration details")
    public void aUserProvidesValidRegistrationDetails() {
        validUserCredentials =
                new UserCredentials(
                        MOCK_USERNAME, MOCK_PASSWORD);

        Assertions.assertNotNull(validUserCredentials);
        Assertions.assertNotNull(validUserCredentials.username());
        Assertions.assertNotNull(validUserCredentials.password());
    }

    @When("the user submits the registration form")
    public void theUserSubmitsTheRegistrationForm() {
        ResponseEntity<Success> responseEntity = userService.signUp(validUserCredentials.username(), validUserCredentials.password());
        responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        Assertions.assertNotNull(responseEntityStatus);
    }

    @Then("the user should be registered successfully")
    public void theUserShouldBeRegisteredSuccessfully() {
        assertEquals(HttpStatus.OK, responseEntityStatus);
    }
}
