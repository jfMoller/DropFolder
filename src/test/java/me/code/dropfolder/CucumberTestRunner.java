package me.code.dropfolder;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * This class serves as a runner for Cucumber tests in the Spring Boot application.
 * It establishes the connection between Gherkin (.feature files) and the corresponding test step definitions
 * in the "cucumberglue" package, and specifies the options for executing the Cucumber tests.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features", // Defines the location of feature files
        glue = "me.code.dropfolder.cucumberglue", // Defines the location of test step definitions
        plugin = {"pretty"} // Defines the output format of the test results
)
public class CucumberTestRunner {
}
