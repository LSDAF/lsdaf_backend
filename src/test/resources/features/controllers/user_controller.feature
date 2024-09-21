Feature: User Controller tests

  Background:
    Given the BDD engine is ready
    And a clean database
    And the time clock set to the present

  Scenario: A non-logged in user tries to get UserInfo
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |
    When the user requests the endpoint to get his UserInfo with no token

    Then the response status code should be 401
  Scenario: A user gets its UserInfo
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |
    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get his UserInfo

    Then the response status code should be 200
  Scenario: A non-logged in user tries to get his GameSaves
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |

    When the user requests the endpoint to get his GameSaves with no token

    Then the response status code should be 401
  Scenario: A user gets its GameSaves
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |
    And the following game saves
      | id                                   | userId                               | gold | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10           | 10     |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 100  | 100          | 100    |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 1000 | 1000         | 1000   |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get his GameSaves

    Then the response status code should be 200