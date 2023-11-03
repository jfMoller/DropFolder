package code.me.dropfolder.cucumberglue;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Configuration class for running Spring Boot tests with Cucumber.
 * It sets up the testing environment with a real embedded web server and assigns a random port.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
/**
 * Provides the context configuration for Cucumber tests.
 * This allows Spring Boot to integrate with Cucumber for testing.
 */
@CucumberContextConfiguration
public class CucumberSpringConfiguration {
}