package me.code.dropfolder.cucumberglue;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Configuration class for running Spring Boot tests with Cucumber.
 * <p>
 * This class is annotated with {@code @SpringBootTest} to indicate that it is a Spring Boot test
 * and to configure the web environment with a random port.
 * The {@code @CucumberContextConfiguration} annotation provides the context configuration for Cucumber tests,
 * allowing Spring Boot to integrate with Cucumber for testing.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class CucumberSpringConfiguration {
}