package code.me.dropfolder.features;

import code.me.dropfolder.dto.UserCredentials;
import code.me.dropfolder.model.User;
import code.me.dropfolder.service.UserService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SignUpFeature {

    private UserService userService;
    private UserCredentials validUserCredentials;
    private ResponseEntity<Object> responseEntity;

    private HttpStatus responseEntityStatus;



    @Given("there is a User")
    public void thereIsAUser() {
        User user = new User();

        assertNotNull(user);
    }

    @Given("there is a UserService")
    public void thereIsAUserService() {
        userService = new UserService();

        assertNotNull(userService);
    }

    @Given("there are UserCredentials")
    public void thereAreUserCredentials() {
        String USERNAME = "keyboardcat-fwnw7236";
        String PASSWORD = "keyboardcat-wyw532791";

        UserCredentials userCredentials = new UserCredentials(USERNAME, PASSWORD);

        assertNotNull(userCredentials);
        assertEquals(USERNAME, userCredentials.username());
        assertEquals(PASSWORD, userCredentials.password());
    }

    @Given("I can enter my valid credentials")
    public void iCanEnterMyValidCredentials() {
        validUserCredentials = new UserCredentials("keyboardcat-fnfwe398234bqd", "keyboardcat-kwefn2364mdq8");

        assertNotNull(validUserCredentials);
        assertNotNull(validUserCredentials.username());
        assertNotNull(validUserCredentials.password());
    }

    @When("my credentials are received")
    public void myCredentialsAreReceived() {
        responseEntity = userService.signUp(validUserCredentials.username(), validUserCredentials.password());
        responseEntityStatus = (HttpStatus) responseEntity.getStatusCode();

        assertNotNull(responseEntityStatus);
    }

    @Then("a new account with my credentials are created")
    public void aNewAccountWithMyCredentialsAreCreated() {
        assertEquals(HttpStatus.OK, responseEntityStatus);
    }


}
