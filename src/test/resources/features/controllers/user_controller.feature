Feature: User Controller BDD tests

  Background:
    Given the BDD engine is ready
    And a clean database
    And the time clock set to the present

  Scenario: A user gets its UserInfo
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get his UserInfo

    Then the response status code should be 200

    And the response should have the following UserInfo
      | email               | name       | roles                | verified |
      | paul.ochon@test.com | Paul OCHON | ROLE_USER,ROLE_ADMIN | true     |