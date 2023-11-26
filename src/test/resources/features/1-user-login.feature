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
