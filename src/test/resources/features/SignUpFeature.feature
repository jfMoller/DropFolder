Feature: Sign up as a new user
  As a user, I want to be able to register an account

  Background:
    Given there is a User
    Given there is a UserService
    Given there are UserCredentials

  Scenario: Enter valid user credentials
    Given I can enter my valid credentials
    When my credentials are received
    Then a new account with my credentials are created
