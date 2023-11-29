package me.code.dropfolder.cucumberglue;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Configuration class for running Spring Boot tests with Cucumber.
 * <p>
 * Uses {@code @TestPropertySource} to set up and connect to the test database.
 * Configures the web environment with a random port using {@code @SpringBootTest}.
 * {@code @CucumberContextConfiguration} allows Spring Boot to integrate with Cucumber for testing.
 */
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class CucumberSpringConfiguration {
}