Feature: Stage Controller BDD tests

  Background:
    Given the BDD engine is ready
    And the cache is enabled
    And a clean database
    And the time clock set to the present

    # We assume we have the two following users in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234
    # paul.itesse@test.com: USER: toto5678

  Scenario: A user gets the stages of one of his game saves with cache
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | maxStage | currentStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 100  | 100     | 100     | 100      | 500          | 1072   | 10       | 10           |
    And the following stage entries in cache
      | gameSaveId                           | currentStage | maxStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 99           | 100      |
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the endpoint to get the stages of the game save with id f81b710d-3e02-4871-a86f-390377798dd1

    Then the response status code should be 200

    And the response should have the following Stage
      | currentStage | maxStage |
      | 99           | 100      |

  Scenario: A user gets the stages of one of his game saves without cache
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | maxStage | currentStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 100  | 100     | 100     | 100      | 500          | 1072   | 10       | 10           |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the stages of the game save with id f81b710d-3e02-4871-a86f-390377798dd1
    Then the response status code should be 200
    And the response should have the following Stage
      | currentStage | maxStage |
      | 10           | 10       |

  Scenario: A user gets the stages of a game save that does not exist
    Given the following game saves
      | id                                   | userEmail           | gold    | healthPoints | attack | maxStage | currentStage | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 500          | 1072   | 10       | 10           | 10      | 10      | 10       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the stages of the game save with id 9d96507e-56e8-447d-aa0f-b2248ae59454

    Then the response status code should be 404

  Scenario: A user gets the stages of a non-owned game save
    Given the following game saves
      | id                                   | userEmail            | gold    | healthPoints | attack | maxStage | currentStage | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.itesse@test.com | 5630280 | 500          | 1072   | 10       | 10           | 10      | 10      | 10       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the stages of the game save with id f81b710d-3e02-4871-a86f-390377798dd1

    Then the response status code should be 403

  Scenario: A user tries to set the stages of one of his game saves with invalid data
    Given the following game saves
      | id                                   | userEmail           | gold    | diamond | emerald | amethyst | healthPoints | attack | maxStage | currentStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 5630280 | 5630280 | 5630280  | 500          | 1072   | 10       | 10           |
    And the following stage entries in cache
      | gameSaveId                           | currentStage | maxStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 666          | 667      |
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to set the stages with the following StageRequest for the game save with id f81b710d-3e02-4871-a86f-390377798dd1
      | currentStage | maxStage |
      | 700          | 600      |

    Then the response status code should be 400

  Scenario: A user sets the stages of one of his game saves with cache
    Given the following game saves
      | id                                   | userEmail           | gold    | diamond | emerald | amethyst | healthPoints | attack | maxStage | currentStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 5630280 | 5630280 | 5630280  | 500          | 1072   | 10       | 10           |
    And the following stage entries in cache
      | gameSaveId                           | currentStage | maxStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 666          | 667      |
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to set the stages with the following StageRequest for the game save with id f81b710d-3e02-4871-a86f-390377798dd1
      | currentStage | maxStage |
      | 700          | 700      |

    Then the response status code should be 200

    And the following stage entries in cache
      | gameSaveId                           | currentStage | maxStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 700          | 700      |

  Scenario: A user sets the stages of one of his game saves without cache
    Given the following game saves
      | id                                   | userEmail           | gold    | healthPoints | attack | maxStage | currentStage | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 500          | 1072   | 10       | 10           | 10      | 10      | 10       |

    And the cache is disabled

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to set the stages with the following StageRequest for the game save with id f81b710d-3e02-4871-a86f-390377798dd1
      | gameSaveId                           | currentStage | maxStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 667          | 700      |

    Then the response status code should be 200

  Scenario: A user sets the stages of a non-owned game save
    Given the following game saves
      | id                                   | userEmail            | gold    | healthPoints | attack | maxStage | currentStage | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.itesse@test.com | 5630280 | 500          | 1072   | 10       | 10           | 10      | 10      | 10       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to set the stages with the following StageRequest for the game save with id f81b710d-3e02-4871-a86f-390377798dd1
      | gameSaveId                           | currentStage | maxStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 667          | 700      |

    Then the response status code should be 403

  Scenario: A user sets the stages of a non-existing game save
    Given the following game saves
      | id                                   | userEmail           | gold    | healthPoints | attack | maxStage | currentStage | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | paul.ochon@test.com | 5630280 | 500          | 1072   | 10       | 10           | 10      | 10      | 10       |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to set the stages with the following StageRequest for the game save with id 7545eed0-237c-4182-849f-f9d4e1d112b5
      | gameSaveId                           | currentStage | maxStage |
      | 7545eed0-237c-4182-849f-f9d4e1d112b5 | 667          | 700      |

    Then the response status code should be 404