package code.me.dropfolder;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "code.me.dropfolder.cucumberglue",
        plugin = {"pretty"}
)
public class CucumberTestRunner {
}