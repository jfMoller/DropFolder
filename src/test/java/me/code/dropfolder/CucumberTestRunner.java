package me.code.dropfolder;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * This class serves as a runner for Cucumber tests in the Spring Boot application.
 * It establishes the connection between Gherkin (.feature files) and corresponding step definitions
 * in the "cucumberglue" package, and specifies the options for executing the Cucumber tests.
 */

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features", // Defines the location of feature files
        glue = "me.code.dropfolder.cucumberglue", // Defines the location of step definitions
        plugin = {"pretty"} // Define the output format of the test results
)
public class CucumberTestRunner {
}
