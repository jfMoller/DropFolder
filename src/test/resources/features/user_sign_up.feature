Feature: User Registration
  As a user, I want to be able to register an account

  Scenario Outline: Successful user registration
    Given a user provides valid registration details with username "<username>" and password "<password>"
    When the user submits the registration form
    Then the user should be registered successfully

    Examples:
      | username           | password           |
      | validusername1     | Valid_password_1   |

  Scenario Outline: Unsuccessful user registration (Invalid username)
    Given a user provides invalid registration details with username "<username>" and password "<password>"
    When the user submits the registration form
    Then the registration should fail

    Examples:
      | username           | password           |
      | _@?!_%             | Valid_password_1   |
      | 12                 | Valid_password_2   |
      |                    | Valid_password_3   |

  Scenario Outline: Unsuccessful user registration (Invalid password)
    Given a user provides invalid registration details with username "<username>" and password "<password>"
    When the user submits the registration form
    Then the registration should fail

    Examples:
      | username           | password           |
      | validusername1     |                    |
      | validusername2     | short              |
      | validusername3     | no-uppercase       |
