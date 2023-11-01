Feature: User Registration
  As a user, I want to be able to register an account

  Scenario: Successful user registration
    Given a user provides valid registration details
    When the user submits the registration form
    Then the user should be registered successfully
