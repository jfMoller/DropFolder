Feature: User Login
  As a user, I want to be able to login to my account

  @cleanupLoginData
  Scenario Outline: Successful user login
    Given the user has a registered account with username "<username>" and password "<password>"
    Then the user successfully logs in with username "<username>" and password "<password>"

    Examples:
      | username  | password   |
      | username1 | Password_1 |
      | username2 | Password_2 |
      | username3 | Password_3 |
      | username4 | Password_4 |
      | username5 | Password_5 |

  @cleanupLoginData
  Scenario Outline: Unsuccessful registered user login
    Given the user has a registered account with username "<username>" and password "<password>"
    Then the user can not log in with a valid username "<username>" and an invalid valid password "<invalid_password>"
    And the user can not log in with an invalid username "<invalid_username>" and a valid password "<password>"

    Examples:
      | username  | password   | invalid_username | invalid_password |
      | username1 | Password_1 | usern            | Passw            |
      | username2 | Password_2 | userna           | Passwo           |
      | username3 | Password_3 | userna           | Passwor          |
      | username4 | Password_4 | usernam          | Password         |
      | username5 | Password_5 | username         | Password_        |

  Scenario Outline: Unsuccessful non-registered user login
    Given the user does not have a registered account with username "<username>"
    Then the user can not log in with a non-registered username "<username>" and a non-registered password "<password>"

    Examples:
      | username  | password   |
      | username1 | Password_1 |
      | username2 | Password_2 |
      | username3 | Password_3 |
      | username4 | Password_4 |
      | username5 | Password_5 |
