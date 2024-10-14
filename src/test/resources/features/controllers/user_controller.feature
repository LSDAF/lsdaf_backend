Feature: User Controller tests

  Background:
    Given the BDD engine is ready
    And a clean database
    And the time clock set to the present

  Scenario: A non-logged in user tries to get UserInfo
    Given the following users
      | id                                   | name       | email               | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     | USER  |
    When the user requests the endpoint to get his UserInfo with no token

    Then the response status code should be 401

  Scenario: A user gets its UserInfo
    Given the following users
      | id                                   | name       | email               | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     | USER  |
    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get his UserInfo

    Then the response status code should be 200

  Scenario: A non-logged in user tries to get his GameSaves
    Given the following users
      | id                                   | name       | email               | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     | USER  |

    When the user requests the endpoint to get his GameSaves with no token

    Then the response status code should be 401

  Scenario: A user gets its GameSaves without cached data
    Given the following users
      | id                                   | name       | email               | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | maxStage | currentStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 10   | 100     | 1000    | 10000    | 10           | 10     | 100      | 99           |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 100  | 1000    | 10000   | 100000   | 100          | 100    | 1000     | 999          |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 1000 | 10000   | 100000  | 1000000  | 1000         | 1000   | 10000    | 9999         |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get his GameSaves

    Then the response status code should be 200

    And the response should have the following GameSaves
      | id                                   | userId                               | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | maxStage | currentStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 10   | 100     | 1000    | 10000    | 10           | 10     | 100      | 99           |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 100  | 1000    | 10000   | 100000   | 100          | 100    | 1000     | 999          |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 1000 | 10000   | 100000  | 1000000  | 1000         | 1000   | 10000    | 9999         |

  Scenario: A user gets its GameSaves with cached data
    Given the following users
      | id                                   | name       | email               | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     | USER  |
    And the following game saves
      | id                                   | userEmail           | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | maxStage | currentStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 100     | 1000    | 10000    | 10           | 10     | 100      | 99           |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | paul.ochon@test.com | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 100  | 1000    | 10000   | 100000   | 100          | 100    | 1000     | 999          |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | paul.ochon@test.com | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 1000 | 10000   | 100000  | 1000000  | 1000         | 1000   | 10000    | 9999         |

    And the following currency entries in cache
      | gameSaveId                           | gold  | diamond | emerald | amethyst |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 10000 | 100000  | 1000000 | 10000000 |

    And the following stage entries in cache
      | gameSaveId                           | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 100          | 100      |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get his GameSaves

    Then the response status code should be 200

    And the response should have the following GameSaves
      | id                                   | userId                               | userEmail           | gold  | diamond | emerald | amethyst | healthPoints | attack | maxStage | currentStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 10000 | 100000  | 1000000 | 10000000 | 10           | 10     | 100      | 100          |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 100   | 1000    | 10000   | 100000   | 100          | 100    | 1000     | 999          |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 1000  | 10000   | 100000  | 1000000  | 1000         | 1000   | 10000    | 9999         |

