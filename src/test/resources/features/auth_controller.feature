Feature: AuthController integration tests

  Background:
    Given the BDD engine is ready
    And a clean database

  Scenario: A User registers its account with valid data
    When the user requests the endpoint to register a user with the following UserCreationRequest
      | email               | name      | password |
      | paul.ochon@test.com | Toto Toto | totototo |
    Then the response status code should be 200
    And the response should have the following UserInfo
      | email               | name      | roles |
      | paul.ochon@test.com | Toto Toto | USER  |


  Scenario: A User registers its account with already used email
    Given the following users
      | id                                   | name       | email               |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com |
    When the user requests the endpoint to register a user with the following UserCreationRequest
      | email               | name      | password |
      | paul.ochon@test.com | Toto Toto | totototo |
    Then the response status code should be 400


  Scenario: A User registers its account with invalid password
    When the user requests the endpoint to register a user with the following UserCreationRequest
      | email               | name      | password |
      | paul.ochon@test.com | Toto Toto | 1234     |
    Then the response status code should be 400


  Scenario: A User registers its account with invalid name
    When the user requests the endpoint to register a user with the following UserCreationRequest
      | email               | name | password |
      | paul.ochon@test.com |      | totototo |
    Then the response status code should be 400


  Scenario: A User registers its account with invalid email format-
    When the user requests the endpoint to register a user with the following UserCreationRequest
      | email      | name      | password |
      | paul.ochon | Toto Toto | totototo |
    Then the response status code should be 400