Feature: User Registration
  As a user, I want to be able to register an account

  @cleanupRegistrationData
  Scenario Outline: Successful user registration
    Given the user provides valid registration credentials with username "<username>" and password "<password>"
    When the user submits the registration
    Then the user should be registered successfully

    Examples:
      | username       | password         | description
      | validusername1 | Valid_password_1 | # Valid username and password

  Scenario Outline: Unsuccessful user registration
    Given the user provides invalid registration credentials with username "<username>" and password "<password>"
    When the user submits the registration
    Then the registration should fail

    Examples:
      | username           | password           | description
      | _@?!_%             | Valid_password_1   | # Invalid characters in username
      | toolongofausername | Valid_password_2   | # Invalid username length (too long)
      | 12                 | Valid_password_3   | # Invalid username length (too short)
      |                    | Valid_password_3   | # Blank username
      | set_as_null        | Valid_password_3   | # Username is null
      | validusername1     |                    | # Password is blank
      | validusername2     | short              | # Invalid password length (too short)
      | validusername3     | no-uppercase       | # No uppercase characters in password
      | validusername4     | Passwordistooloong | # Invalid password length (too long)
      | validusername5     | set_as_null        | # Password is null
